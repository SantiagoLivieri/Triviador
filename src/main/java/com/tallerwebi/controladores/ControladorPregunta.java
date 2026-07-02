package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/disputa")
public class ControladorPregunta {

  private final ServicioJuego servicioJuego;
  private final ServicioProvincia servicioProvincia;
  private final ServicioPregunta servicioPregunta;

  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";
  private static final String MENSAJE_RESULTADO = "mensajeResultado";
  private static final String REDIRECT_TABLERO = "redirect:/juego/partida/";
  private static final String REDIRECT_PREGUNTA_ACTUAL =
    "redirect:/disputa/pregunta-actual?partidaId=";
  private static final String REDIRECT_RESULTADOS = "redirect:/juego/partida/resultados/";

  public static final String REQUERIDAS_ATTR = "preguntasRequeridas";
  public static final String RESPONDIDAS_ATTR = "preguntasRespondidasExito";
  private static final String PREGUNTA_ACTUAL = "preguntaActual";
  private static final String OPCIONES_ACTUALES = "opcionesActuales";

  private static final String DOBLE_CHANCE_ACTIVA = "dobleChanceActivo";
  private static final String ERROR_COMODIN = "errorComodin";
  private static final String MENSAJE_COMODIN = "mensajeComodin";
  private static final String COMODIN_YA_USADO = "comodinUsadoEnEstaPregunta";

  @Autowired
  public ControladorPregunta(
    ServicioJuego servicioJuego,
    ServicioProvincia servicioProvincia,
    ServicioPregunta servicioPregunta
  ) {
    this.servicioJuego = servicioJuego;
    this.servicioProvincia = servicioProvincia;
    this.servicioPregunta = servicioPregunta;
  }

  @PostMapping("/seleccionar-provincia")
  public ModelAndView seleccionarProvincia(
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    try {
      Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

      if (partida.getJugadorEnTurno() == null && !partida.getJugadores().isEmpty()) {
        partida.setJugadorEnTurno(partida.getJugadores().get(0));
        servicioJuego.actualizarPartida(partida);
      }

      // 3. Iniciamos el ataque
      servicioJuego.iniciarAtaque(partidaId, idProvincia);

      Set<Long> preguntasHechas = servicioJuego.obtenerPreguntasHechas(partidaId);
      Pregunta pregunta = servicioPregunta.obtenerPreguntaPorProvincia(
        idProvincia,
        preguntasHechas
      );

      if (pregunta == null) {
        session.setAttribute(MENSAJE_RESULTADO, "No hay preguntas cargadas.");
        return new ModelAndView(REDIRECT_TABLERO + partidaId);
      }

      servicioJuego.registrarPreguntaHecha(partidaId, pregunta.getId());

      session.setAttribute(
        REQUERIDAS_ATTR,
        servicioJuego.obtenerCantidadPreguntasRequeridas(idProvincia)
      );
      session.setAttribute(RESPONDIDAS_ATTR, 0);
      session.setAttribute(PREGUNTA_ACTUAL, pregunta);
      session.setAttribute(OPCIONES_ACTUALES, servicioPregunta.obtenerOpcionesMezcladas(pregunta));
      session.setAttribute("idProvinciaActual", idProvincia);

      session.removeAttribute(COMODIN_YA_USADO);
      session.removeAttribute(DOBLE_CHANCE_ACTIVA);

      return new ModelAndView(REDIRECT_PREGUNTA_ACTUAL + partidaId);
    } catch (Exception e) {
      session.setAttribute(MENSAJE_RESULTADO, e.getMessage());
      return new ModelAndView("redirect:/juego/partida/" + partidaId);
    }
  }

  @GetMapping("/pregunta-actual")
  public ModelAndView mostrarPreguntaActual(
    @RequestParam("partidaId") Long partidaId,
    HttpSession session
  ) {
    Pregunta pregunta = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);
    if (pregunta == null) {
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    Long idProvinciaActual = (Long) session.getAttribute("idProvinciaActual");
    Provincia provincia = idProvinciaActual != null
      ? servicioProvincia.buscarPorId(idProvinciaActual)
      : pregunta.getProvincia();

    if (provincia == null) {
      session.setAttribute(MENSAJE_RESULTADO, "No se encontro la provincia seleccionada.");
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    Partida partidaActualizada = servicioJuego.obtenerPartidaPorId(partidaId);

    ModelMap modelo = new ModelMap();
    modelo.put("partida", partidaActualizada);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put("pregunta", pregunta);
    modelo.put("jugadorActual", partidaActualizada.getJugadorEnTurno());
    modelo.put("idProvincia", provincia.getId());
    modelo.put("opciones", session.getAttribute(OPCIONES_ACTUALES));
    modelo.put("provincia", provincia);

    return new ModelAndView("pregunta", modelo);
  }

  @PostMapping("/responder-provincia")
  public ModelAndView responderProvincia(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam("idPregunta") Long idPregunta,
    @RequestParam("respuesta") String respuesta,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Boolean acerto = servicioPregunta.validarRespuesta(idPregunta, respuesta);

    if (!acerto) {
      Boolean dobleChanceActiva = (Boolean) session.getAttribute(DOBLE_CHANCE_ACTIVA);
      boolean tieneDobleChance = dobleChanceActiva != null && dobleChanceActiva;
      if (tieneDobleChance) {
        session.setAttribute(DOBLE_CHANCE_ACTIVA, false);

        @SuppressWarnings("unchecked")
        List<String> opciones = (List<String>) session.getAttribute(OPCIONES_ACTUALES);

        session.setAttribute(
          OPCIONES_ACTUALES,
          servicioPregunta.removerOpcionIncorrecta(opciones, respuesta)
        );

        flash.addFlashAttribute(
          MENSAJE_COMODIN,
          "¡Respuesta incorrecta, pero la Doble Chance te salvó! Te queda un intento."
        );
        return new ModelAndView(REDIRECT_PREGUNTA_ACTUAL + partidaId);
      } else {
        session.setAttribute(MENSAJE_RESULTADO, "Respuesta incorrecta. Fin de tu turno.");
        limpiarSesionDisputa(session);

        servicioJuego.avanzarTurno(partidaId);

        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)) {
          return new ModelAndView(REDIRECT_RESULTADOS + partidaId);
        }
        return new ModelAndView(REDIRECT_TABLERO + partidaId);
      }
    }

    Integer respondidas = (Integer) session.getAttribute(RESPONDIDAS_ATTR);
    Integer requeridas = (Integer) session.getAttribute(REQUERIDAS_ATTR);
    Integer nuevasRespondidas = (respondidas != null ? respondidas : 0) + 1;

    session.setAttribute(RESPONDIDAS_ATTR, nuevasRespondidas);

    String mensajeVictoria = servicioJuego.evaluarAcierto(
      partidaId,
      idProvincia,
      nuevasRespondidas,
      requeridas
    );

    if (mensajeVictoria != null) {
      session.setAttribute(MENSAJE_RESULTADO, mensajeVictoria);
      limpiarSesionDisputa(session);

      Long usuarioId = (Long) session.getAttribute("usuarioId");
      if (servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)) {
        return new ModelAndView(REDIRECT_RESULTADOS + partidaId);
      }
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    } else {
      Set<Long> preguntasHechas = servicioJuego.obtenerPreguntasHechas(partidaId);
      Pregunta proximaPregunta = servicioPregunta.obtenerPreguntaPorProvincia(
        idProvincia,
        preguntasHechas
      );

      servicioJuego.registrarPreguntaHecha(partidaId, proximaPregunta.getId());

      session.setAttribute(PREGUNTA_ACTUAL, proximaPregunta);
      session.setAttribute(
        OPCIONES_ACTUALES,
        servicioPregunta.obtenerOpcionesMezcladas(proximaPregunta)
      );
      session.removeAttribute(COMODIN_YA_USADO);
      session.removeAttribute(DOBLE_CHANCE_ACTIVA);

      return new ModelAndView(REDIRECT_PREGUNTA_ACTUAL + partidaId);
    }
  }

  @PostMapping("/usar-comodin")
  public ModelAndView usarComodin(
    @RequestParam("tipoComodin") String tipoComodin,
    @RequestParam("partidaId") Long partidaId,
    HttpSession session,
    RedirectAttributes flash
  ) {
    try {
      ejecutarLogicaDeComodin(tipoComodin, partidaId, session, flash, getIdUsuario(session));
    } catch (IllegalStateException | IllegalArgumentException e) {
      flash.addFlashAttribute(ERROR_COMODIN, e.getMessage());
    }
    return new ModelAndView(REDIRECT_PREGUNTA_ACTUAL + partidaId);
  }

  private void ejecutarLogicaDeComodin(
    String tipoComodin,
    Long partidaId,
    HttpSession session,
    RedirectAttributes flash,
    Long idUsuario
  ) {
    if (esJugadorInvitado(partidaId)) {
      flash.addFlashAttribute(ERROR_COMODIN, "Solo el anfitrión puede gastar comodines.");
      return;
    }

    final Pregunta preguntaActual = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);
    if (preguntaActual == null) {
      flash.addFlashAttribute(ERROR_COMODIN, "No hay pregunta activa.");
      return;
    }

    if (Boolean.TRUE.equals((Boolean) session.getAttribute(COMODIN_YA_USADO))) {
      flash.addFlashAttribute(ERROR_COMODIN, "Ya utilizaste un comodín en esta pregunta.");
      return;
    }

    aplicarEfectoComodin(tipoComodin, session, flash, idUsuario, preguntaActual);

    if (!flash.getFlashAttributes().containsKey(ERROR_COMODIN)) {
      final Usuario usuarioActualizado = servicioJuego.obtenerUsuarioPorId(idUsuario);
      session.setAttribute("usuarioLogueado", usuarioActualizado);
    }
  }

  private boolean esJugadorInvitado(Long partidaId) {
    final Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    return (
      partida != null &&
      partida.getJugadorEnTurno() != null &&
      partida.getJugadorEnTurno().getUsuario() == null
    );
  }

  private void aplicarEfectoComodin(
    String tipoComodin,
    HttpSession session,
    RedirectAttributes flash,
    Long idUsuario,
    Pregunta preguntaActual
  ) {
    switch (tipoComodin) {
      case "ELIMINAR_2":
        {
          @SuppressWarnings("unchecked")
          final List<String> opcionesEnPantalla = (List<String>) session.getAttribute(
            OPCIONES_ACTUALES
          );

          if (opcionesEnPantalla == null || opcionesEnPantalla.size() <= 2) {
            flash.addFlashAttribute(ERROR_COMODIN, "Ya usaste este comodín.");
            break;
          }

          final List<String> opcionesFiltradas = servicioPregunta.aplicarComodinEliminarDos(
            idUsuario,
            opcionesEnPantalla,
            preguntaActual
          );

          session.setAttribute(OPCIONES_ACTUALES, opcionesFiltradas);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute(
            MENSAJE_COMODIN,
            "¡Comodín aplicado! Se eliminaron 2 respuestas incorrectas."
          );
          break;
        }
      case "DOBLE_CHANCE":
        {
          if (Boolean.TRUE.equals((Boolean) session.getAttribute(DOBLE_CHANCE_ACTIVA))) {
            flash.addFlashAttribute(
              ERROR_COMODIN,
              "Ya tenes la Doble Chance activada para este turno."
            );
            break;
          }

          servicioPregunta.aplicarComodinDobleChance(idUsuario);

          session.setAttribute(DOBLE_CHANCE_ACTIVA, true);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute(MENSAJE_COMODIN, "¡Doble Chance activada!");
          break;
        }
      case "PASAR_PREGUNTA":
        {
          final Set<Long> preguntasYaHechas = recuperarPreguntasYaHechas(session);
          final Long idProvinciaActual = (Long) session.getAttribute("idProvinciaActual");

          final Pregunta nuevaPregunta = servicioPregunta.aplicarComodinPasarPregunta(
            idUsuario,
            preguntaActual,
            idProvinciaActual,
            preguntasYaHechas
          );

          final List<String> nuevasOpciones = servicioPregunta.obtenerOpcionesMezcladas(
            nuevaPregunta
          );

          session.setAttribute(PREGUNTA_ACTUAL, nuevaPregunta);
          session.setAttribute(OPCIONES_ACTUALES, nuevasOpciones);
          session.setAttribute("preguntasYaHechas", preguntasYaHechas);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute(MENSAJE_COMODIN, "¡Pregunta saltada exitosamente!");
          break;
        }
      default:
        flash.addFlashAttribute(ERROR_COMODIN, "El tipo de comodín no es reconocido.");
        break;
    }
  }

  private void limpiarSesionDisputa(HttpSession session) {
    session.removeAttribute(REQUERIDAS_ATTR);
    session.removeAttribute(RESPONDIDAS_ATTR);
  }

  private Long getIdUsuario(HttpSession session) {
    return ((Usuario) session.getAttribute("usuarioLogueado")).getId();
  }

  private Set<Long> recuperarPreguntasYaHechas(HttpSession session) {
    @SuppressWarnings("unchecked")
    Set<Long> historial = (Set<Long>) session.getAttribute("preguntasYaHechas");
    return historial != null ? historial : new java.util.HashSet<>();
  }
}
