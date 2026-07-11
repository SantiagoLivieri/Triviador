package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
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
  private final ServicioRespuestaPartida servicioRespuestaPartida;

  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";
  private static final String ID_PROVINCIA_ACTUAL = "idProvinciaActual";
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

  private static final String ID_PROVINCIA_PARAM = "idProvincia";
  private static final String PREGUNTA_VIEW_ATTR = "pregunta";
  private static final String PARTIDA_VIEW_ATTR = "partida";
  private static final String SUPPRESS_UNCHECKED = "unchecked";
  private static final String JUGADOR_ACTUAL_VIEW_ATTR = "jugadorActual";
  private static final String OPCIONES_VIEW_ATTR = "opciones";
  private static final String PROVINCIA_VIEW_ATTR = "provincia";

  private static final String DESTINO_TABLERO = "TABLERO";
  private static final String DESTINO_RESULTADOS = "RESULTADOS";
  private static final String DESTINO_PREGUNTA = "PREGUNTA";

  @Autowired
  public ControladorPregunta(
    ServicioJuego servicioJuego,
    ServicioProvincia servicioProvincia,
    ServicioPregunta servicioPregunta,
    ServicioRespuestaPartida servicioRespuestaPartida
  ) {
    this.servicioJuego = servicioJuego;
    this.servicioProvincia = servicioProvincia;
    this.servicioPregunta = servicioPregunta;
    this.servicioRespuestaPartida = servicioRespuestaPartida;
  }

  @PostMapping("/seleccionar-provincia")
  public ModelAndView seleccionarProvincia(
    @RequestParam(ID_PROVINCIA_PARAM) Long idProvincia,
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
      session.setAttribute(ID_PROVINCIA_ACTUAL, idProvincia);

      session.removeAttribute(COMODIN_YA_USADO);
      session.removeAttribute(DOBLE_CHANCE_ACTIVA);

      return new ModelAndView(REDIRECT_PREGUNTA_ACTUAL + partidaId);
    } catch (Exception e) {
      session.setAttribute(MENSAJE_RESULTADO, e.getMessage());
      return new ModelAndView("redirect:/juego/partida/" + partidaId);
    }
  }

  @PostMapping("/continuar-feedback-incorrecto")
  public ModelAndView continuarFeedbackIncorrecto(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    limpiarSesionDisputa(session);

    servicioJuego.avanzarTurno(partidaId);

    Long usuarioId = (Long) session.getAttribute("usuarioId");

    if (servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)) {
      return new ModelAndView(REDIRECT_RESULTADOS + partidaId);
    }

    return new ModelAndView(REDIRECT_TABLERO + partidaId);
  }

  @GetMapping("/pregunta-actual")
  public ModelAndView mostrarPreguntaActual(
    @RequestParam("partidaId") Long partidaId,
    HttpSession session,
    HttpServletResponse response
  ) {
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Pregunta pregunta = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);
    if (pregunta == null) {
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    Long idProvinciaActual = (Long) session.getAttribute(ID_PROVINCIA_ACTUAL);
    Provincia provincia = idProvinciaActual != null
      ? servicioProvincia.buscarPorId(idProvinciaActual)
      : pregunta.getProvincia();

    if (provincia == null) {
      session.setAttribute(MENSAJE_RESULTADO, "No se encontro la provincia seleccionada.");
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    Partida partidaActualizada = servicioJuego.obtenerPartidaPorId(partidaId);

    ModelMap modelo = new ModelMap();
    modelo.put(PARTIDA_VIEW_ATTR, partidaActualizada);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put(PREGUNTA_VIEW_ATTR, pregunta);
    modelo.put(JUGADOR_ACTUAL_VIEW_ATTR, partidaActualizada.getJugadorEnTurno());
    modelo.put(ID_PROVINCIA_PARAM, provincia.getId());
    modelo.put(OPCIONES_VIEW_ATTR, session.getAttribute(OPCIONES_ACTUALES));
    modelo.put(PROVINCIA_VIEW_ATTR, provincia);

    return new ModelAndView(PREGUNTA_VIEW_ATTR, modelo);
  }

  @PostMapping("/responder-provincia")
  public ModelAndView responderProvincia(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    @RequestParam(ID_PROVINCIA_PARAM) Long idProvincia,
    @RequestParam("idPregunta") Long idPregunta,
    @RequestParam("respuesta") String respuesta,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Pregunta preguntaActual = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);
    Integer requeridas = (Integer) session.getAttribute(REQUERIDAS_ATTR);

    if (preguntaActual == null || requeridas == null) {
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    if (!preguntaActual.getId().equals(idPregunta)) {
      return new ModelAndView(REDIRECT_TABLERO + partidaId);
    }

    Boolean acerto = servicioPregunta.validarRespuesta(idPregunta, respuesta);

    servicioRespuestaPartida.registrarOActualizarRespuesta(
      partidaId,
      idPregunta,
      respuesta,
      Boolean.TRUE.equals(acerto)
    );

    if (!acerto) {
      return procesarRespuestaIncorrecta(partidaId, respuesta, session, flash);
    }

    return procesarRespuestaCorrecta(partidaId, idProvincia, session);
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
          @SuppressWarnings(SUPPRESS_UNCHECKED)
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
          final Long idProvinciaActual = (Long) session.getAttribute(ID_PROVINCIA_ACTUAL);

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

    session.removeAttribute(PREGUNTA_ACTUAL);
    session.removeAttribute(OPCIONES_ACTUALES);

    session.removeAttribute(ID_PROVINCIA_ACTUAL);

    session.removeAttribute(COMODIN_YA_USADO);
    session.removeAttribute(DOBLE_CHANCE_ACTIVA);
  }

  private Long getIdUsuario(HttpSession session) {
    return ((Usuario) session.getAttribute("usuarioLogueado")).getId();
  }

  private Set<Long> recuperarPreguntasYaHechas(HttpSession session) {
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    Set<Long> historial = (Set<Long>) session.getAttribute("preguntasYaHechas");
    return historial != null ? historial : new java.util.HashSet<>();
  }

  private ModelAndView procesarRespuestaIncorrecta(
    Long partidaId,
    String respuesta,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Boolean dobleChanceActiva = (Boolean) session.getAttribute(DOBLE_CHANCE_ACTIVA);
    boolean tieneDobleChance = dobleChanceActiva != null && dobleChanceActiva;

    if (tieneDobleChance) {
      session.setAttribute(DOBLE_CHANCE_ACTIVA, false);

      @SuppressWarnings(SUPPRESS_UNCHECKED)
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
    }

    Pregunta preguntaRespondida = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);

    @SuppressWarnings(SUPPRESS_UNCHECKED)
    List<String> opcionesRespondidas = (List<String>) session.getAttribute(OPCIONES_ACTUALES);

    Long idProvincia = (Long) session.getAttribute(ID_PROVINCIA_ACTUAL);

    return mostrarFeedbackIncorrecto(
      partidaId,
      idProvincia,
      preguntaRespondida,
      opcionesRespondidas
    );
  }

  private ModelAndView procesarRespuestaCorrecta(
    Long partidaId,
    Long idProvincia,
    HttpSession session
  ) {
    // Guardamos la pregunta y las opciones que acaba de responder
    // para poder mostrarlas detrás del feedback.
    Pregunta preguntaRespondida = (Pregunta) session.getAttribute(PREGUNTA_ACTUAL);

    @SuppressWarnings(SUPPRESS_UNCHECKED)
    List<String> opcionesRespondidas = (List<String>) session.getAttribute(OPCIONES_ACTUALES);

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

      Long usuarioId = (Long) session.getAttribute("usuarioId");

      final String destinoFeedback = servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)
        ? DESTINO_RESULTADOS
        : DESTINO_TABLERO;

      limpiarSesionDisputa(session);

      return mostrarFeedbackProvinciaGanada(
        partidaId,
        idProvincia,
        preguntaRespondida,
        opcionesRespondidas,
        destinoFeedback,
        mensajeVictoria
      );
    }

    // Si todavía necesita responder más preguntas,
    // dejamos preparada la próxima en sesión.
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

    return mostrarFeedbackCorrecto(
      partidaId,
      idProvincia,
      preguntaRespondida,
      opcionesRespondidas,
      DESTINO_PREGUNTA
    );
  }

  private ModelAndView mostrarFeedbackCorrecto(
    Long partidaId,
    Long idProvincia,
    Pregunta preguntaRespondida,
    List<String> opcionesRespondidas,
    String destinoFeedback
  ) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    ModelMap modelo = new ModelMap();

    modelo.put(PARTIDA_VIEW_ATTR, partida);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put(PREGUNTA_VIEW_ATTR, preguntaRespondida);
    modelo.put(JUGADOR_ACTUAL_VIEW_ATTR, partida.getJugadorEnTurno());
    modelo.put(ID_PROVINCIA_PARAM, idProvincia);
    modelo.put(OPCIONES_VIEW_ATTR, opcionesRespondidas);
    modelo.put(PROVINCIA_VIEW_ATTR, provincia);

    modelo.put("feedbackCorrecto", true);
    modelo.put("destinoFeedback", destinoFeedback);

    return new ModelAndView(PREGUNTA_VIEW_ATTR, modelo);
  }

  private ModelAndView mostrarFeedbackIncorrecto(
    Long partidaId,
    Long idProvincia,
    Pregunta preguntaRespondida,
    List<String> opcionesRespondidas
  ) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    ModelMap modelo = new ModelMap();

    modelo.put(PARTIDA_VIEW_ATTR, partida);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put(PREGUNTA_VIEW_ATTR, preguntaRespondida);
    modelo.put(JUGADOR_ACTUAL_VIEW_ATTR, partida.getJugadorEnTurno());
    modelo.put(ID_PROVINCIA_PARAM, idProvincia);
    modelo.put(OPCIONES_VIEW_ATTR, opcionesRespondidas);
    modelo.put(PROVINCIA_VIEW_ATTR, provincia);

    modelo.put("feedbackIncorrecto", true);

    return new ModelAndView(PREGUNTA_VIEW_ATTR, modelo);
  }

  private ModelAndView mostrarFeedbackProvinciaGanada(
    Long partidaId,
    Long idProvincia,
    Pregunta preguntaRespondida,
    List<String> opcionesRespondidas,
    String destinoFeedback,
    String mensajeVictoria
  ) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    ModelMap modelo = new ModelMap();

    modelo.put(PARTIDA_VIEW_ATTR, partida);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put(PREGUNTA_VIEW_ATTR, preguntaRespondida);
    modelo.put(JUGADOR_ACTUAL_VIEW_ATTR, partida.getJugadorEnTurno());
    modelo.put(ID_PROVINCIA_PARAM, idProvincia);
    modelo.put(OPCIONES_VIEW_ATTR, opcionesRespondidas);
    modelo.put(PROVINCIA_VIEW_ATTR, provincia);

    modelo.put("feedbackCorrecto", true);
    modelo.put("feedbackProvinciaGanada", true);
    modelo.put("mensajeVictoria", mensajeVictoria);
    modelo.put("destinoFeedback", destinoFeedback);

    return new ModelAndView(PREGUNTA_VIEW_ATTR, modelo);
  }
}
