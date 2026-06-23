package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorJuego {

  private final ServicioJuego servicioJuego;
  private final ServicioProvincia servicioProvincia;
  private final ServicioPregunta servicioPregunta;
  private static final String REDIRECT_JUEGO = "redirect:/juego?id=";
  private static final String MENSAJE_RESULTADO = "mensajeResultado";
  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";
  public static final String REQUERIDAS_ATTR = "preguntasRequeridas";
  public static final String RESPONDIDAS_ATTR = "preguntasRespondidasExito";
  private static final String OPCIONES_ACTUALES = "opcionesActuales";
  private static final String DOBLE_CHANCE_ACTIVA = "dobleChanceActivo";

  @Autowired
  public ControladorJuego(
    ServicioJuego servicioJuego,
    ServicioProvincia servicioProvincia,
    ServicioPregunta servicioPregunta
  ) {
    this.servicioJuego = servicioJuego;
    this.servicioProvincia = servicioProvincia;
    this.servicioPregunta = servicioPregunta;
  }

  @PostMapping("/iniciar-partida")
  public ModelAndView iniciarPartida(
    @ModelAttribute("datosLobby") DatosLobby datosLobby,
    HttpSession session
  ) {
    Usuario usuarioAnfitrion = (Usuario) session.getAttribute("usuarioLogueado");
    Long partidaId = servicioJuego.inicializarPartida(datosLobby, usuarioAnfitrion);

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/juego")
  public ModelAndView mostrarJuego(@RequestParam("id") Long partidaId, HttpSession session) {
    ModelMap modelo = new ModelMap();
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    modelo.put("coloresPorJugador", partida.obtenerMapaDeColoresPorJugador());
    modelo.put("partida", partida);
    modelo.put("jugadores", partida.getJugadores());
    modelo.put("provincias", servicioProvincia.obtenerProvincias());
    modelo.put("jugadorActual", partida.getJugadorEnTurno());

    String mensaje = (String) session.getAttribute(MENSAJE_RESULTADO);
    if (mensaje != null) {
      modelo.put(MENSAJE_RESULTADO, mensaje);
      session.removeAttribute(MENSAJE_RESULTADO);
    }

    return new ModelAndView("juego", modelo);
  }

  @PostMapping("/seleccionar-provincia")
  public ModelAndView seleccionarProvincia(
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    Pregunta pregunta = servicioPregunta.obtenerPreguntaPorProvincia(
      idProvincia,
      partida.getPreguntasHechas()
    );

    if (pregunta == null) {
      session.setAttribute(MENSAJE_RESULTADO, "No hay preguntas cargadas.");
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    partida.registrarPreguntaHecha(pregunta.getId());
    servicioJuego.actualizarPartida(partida);

    try {
      servicioJuego.validarAtaque(partida.getJugadorEnTurno().getId(), idProvincia);
      servicioJuego.procesarJugada(partidaId, partida.getJugadorEnTurno().getId(), idProvincia);
    } catch (Exception e) {
      session.setAttribute(MENSAJE_RESULTADO, e.getMessage());
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    session.setAttribute(
      REQUERIDAS_ATTR,
      servicioJuego.obtenerCantidadPreguntasRequeridas(idProvincia)
    );
    session.setAttribute(RESPONDIDAS_ATTR, 0);
    session.setAttribute("preguntaActual", pregunta);
    session.setAttribute(OPCIONES_ACTUALES, servicioPregunta.obtenerOpcionesMezcladas(pregunta));
    session.setAttribute("idProvinciaActual", idProvincia);

    session.removeAttribute("comodinUsadoEnEstaPregunta");
    session.removeAttribute(DOBLE_CHANCE_ACTIVA);

    return new ModelAndView("redirect:/juego/pregunta-actual?partidaId=" + partidaId);
  }

  @GetMapping("/juego/pregunta-actual")
  public ModelAndView mostrarPreguntaActual(
    @RequestParam("partidaId") Long partidaId,
    HttpSession session
  ) {
    Pregunta pregunta = (Pregunta) session.getAttribute("preguntaActual");
    if (pregunta == null) {
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Long idProvinciaActual = (Long) session.getAttribute("idProvinciaActual");
    Provincia provincia = idProvinciaActual != null
      ? servicioProvincia.buscarPorId(idProvinciaActual)
      : pregunta.getProvincia();

    if (provincia == null) {
      session.setAttribute(MENSAJE_RESULTADO, "No se encontro la provincia seleccionada.");
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
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
    Boolean dobleChanceActiva = (Boolean) session.getAttribute(DOBLE_CHANCE_ACTIVA);
    boolean tieneDobleChance = dobleChanceActiva != null && dobleChanceActiva;

    Boolean acerto = servicioJuego.procesarRespuestaYPasarTurno(
      partidaId,
      idProvincia,
      idPregunta,
      respuesta,
      tieneDobleChance
    );

    if (!acerto) {
      if (tieneDobleChance) {
        session.setAttribute(DOBLE_CHANCE_ACTIVA, false);

        @SuppressWarnings("unchecked")
        List<String> opciones = (List<String>) session.getAttribute(OPCIONES_ACTUALES);
        if (opciones != null) {
          opciones.remove(respuesta);
          session.setAttribute(OPCIONES_ACTUALES, opciones);
        }

        flash.addFlashAttribute(
          "mensajeComodin",
          "¡Respuesta incorrecta, pero la Doble Chance te salvó! Te queda un intento."
        );

        return new ModelAndView("redirect:/juego/pregunta-actual?partidaId=" + partidaId);
      } else {
        session.setAttribute(MENSAJE_RESULTADO, "Respuesta incorrecta. Fin de tu turno");
        limpiarSesionDisputa(session);

        return evaluarFinDePartidaORedirigir(partidaId, session, REDIRECT_JUEGO + partidaId);
      }
    }

    Integer respondidas = (Integer) session.getAttribute(RESPONDIDAS_ATTR);
    Integer requeridas = (Integer) session.getAttribute(REQUERIDAS_ATTR);
    Integer nuevasRespondidas = respondidas + 1;
    session.setAttribute(RESPONDIDAS_ATTR, nuevasRespondidas);

    if (servicioJuego.disputaFinalizada(nuevasRespondidas, requeridas)) {
      Partida partidaAntesDeJugar = servicioJuego.obtenerPartidaPorId(partidaId);
      Jugador jugadorQueRespondio = partidaAntesDeJugar.getJugadorEnTurno();

      String mensajeFinal;
      if (servicioJuego.esConquista(requeridas)) {
        servicioJuego.concretarConquista(partidaId, idProvincia);
        mensajeFinal = "¡Respondiste las 3 correctas y conquistaste la provincia!";
      } else {
        servicioJuego.concretarColonizacion(partidaId, idProvincia);
        mensajeFinal = "¡Respuesta correcta! Provincia colonizada.";
      }
      limpiarSesionDisputa(session);

      Partida partidaDespuesDeJugar = servicioJuego.obtenerPartidaPorId(partidaId);
      Jugador jugadorActual = partidaDespuesDeJugar.getJugadorEnTurno();

      if (
        jugadorQueRespondio != null &&
        jugadorActual != null &&
        !jugadorQueRespondio.getId().equals(jugadorActual.getId())
      ) {
        mensajeFinal += "\n\nAlcanzaste el límite máximo de 3 conquistas. Fin de tu turno.";
      }

      session.setAttribute(MENSAJE_RESULTADO, mensajeFinal);
      session.setAttribute("jugadorActual", jugadorActual);

      return evaluarFinDePartidaORedirigir(partidaId, session, REDIRECT_JUEGO + partidaId);
    } else {
      Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
      Pregunta proximaPregunta = servicioPregunta.obtenerPreguntaPorProvincia(
        idProvincia,
        partida.getPreguntasHechas()
      );

      partida.registrarPreguntaHecha(proximaPregunta.getId());
      servicioJuego.actualizarPartida(partida);

      session.setAttribute("preguntaActual", proximaPregunta);
      session.setAttribute(
        OPCIONES_ACTUALES,
        servicioPregunta.obtenerOpcionesMezcladas(proximaPregunta)
      );

      session.removeAttribute("comodinUsadoEnEstaPregunta");
      session.removeAttribute(DOBLE_CHANCE_ACTIVA);

      return new ModelAndView("redirect:/juego/pregunta-actual?partidaId=" + partidaId);
    }
  }

  private void limpiarSesionDisputa(HttpSession session) {
    session.removeAttribute(REQUERIDAS_ATTR);
    session.removeAttribute(RESPONDIDAS_ATTR);
  }

  @RequestMapping(path = "/juego/tiempo-agotado", method = RequestMethod.POST)
  public ModelAndView tiempoAgotado(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    servicioJuego.forzarSaltoPorTiempo(partidaId);
    return evaluarFinDePartidaORedirigir(partidaId, session, REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/partida/resultados/{partidaId}")
  public ModelAndView mostrarResultados(@PathVariable Long partidaId) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    if (!partida.estaFinalizada()) {
      return new ModelAndView("redirect:/partida/tablero/" + partidaId);
    }

    ModelMap modelo = new ModelMap();
    List<Jugador> ranking = partida.obtenerRanking();

    modelo.put("ranking", ranking);
    modelo.put("ganador", ranking.get(0));

    return new ModelAndView("resultados", modelo);
  }

  private ModelAndView evaluarFinDePartidaORedirigir(
    Long partidaId,
    HttpSession session,
    String rutaDefault
  ) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    if (partida.estaFinalizada()) {
      Long usuarioId = (Long) session.getAttribute("usuarioId");
      servicioJuego.finalizarYRegistrarPartida(partidaId, usuarioId);
      return new ModelAndView("redirect:/partida/resultados/" + partidaId);
    }
    return new ModelAndView(rutaDefault);
  }
}
