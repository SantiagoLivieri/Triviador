package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
  private static final int PREGUNTAS_PARA_CONQUISTA = 3;

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
  public ModelAndView iniciarPartida(@ModelAttribute("datosLobby") DatosLobby datosLobby) {
    Long partidaId = servicioJuego.inicializarPartida(datosLobby);
    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/juego")
  public ModelAndView mostrarJuego(@RequestParam("id") Long partidaId, HttpServletRequest request) {
    ModelMap modelo = new ModelMap();

    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    modelo.put("partida", partida);
    modelo.put("jugadores", partida.getJugadores());
    modelo.put("provincias", servicioProvincia.obtenerProvincias());
    modelo.put("jugadorActual", partida.getJugadorEnTurno());

    String mensaje = (String) request.getSession().getAttribute(MENSAJE_RESULTADO);
    if (mensaje != null) {
      modelo.put(MENSAJE_RESULTADO, mensaje);
      request.getSession().removeAttribute(MENSAJE_RESULTADO);
    }

    return new ModelAndView("juego", modelo);
  }

  @PostMapping("/seleccionar-provincia")
  public ModelAndView seleccionarProvincia(
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpServletRequest request
  ) {
    Pregunta pregunta = servicioPregunta.obtenerPreguntaPorProvincia(idProvincia);
    if (pregunta == null) {
      request.getSession().setAttribute(MENSAJE_RESULTADO, "No hay preguntas cargadas.");
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    try {
      servicioJuego.validarAtaque(partidaId, partida.getJugadorEnTurno().getId(), idProvincia);
      servicioJuego.procesarJugada(partidaId, partida.getJugadorEnTurno().getId(), idProvincia);
    } catch (Exception e) {
      request.getSession().setAttribute(MENSAJE_RESULTADO, e.getMessage());
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Integer requeridas = servicioJuego.obtenerCantidadPreguntasRequeridas(idProvincia);
    request.getSession().setAttribute(REQUERIDAS_ATTR, requeridas);

    request.getSession().setAttribute(RESPONDIDAS_ATTR, 0);

    request.getSession().setAttribute("preguntaActual", pregunta);
    request
      .getSession()
      .setAttribute("opcionesActuales", servicioPregunta.obtenerOpcionesMezcladas(pregunta));
    request.getSession().setAttribute("idProvinciaActual", idProvincia);

    return new ModelAndView("redirect:/juego/pregunta-actual?partidaId=" + partidaId);
  }

  @GetMapping("/juego/pregunta-actual")
  public ModelAndView mostrarPreguntaActual(
    @RequestParam("partidaId") Long partidaId,
    HttpServletRequest request
  ) {
    Pregunta pregunta = (Pregunta) request.getSession().getAttribute("preguntaActual");

    if (pregunta == null) {
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Long idProvinciaActual = (Long) request.getSession().getAttribute("idProvinciaActual");

    Provincia provincia = idProvinciaActual != null
      ? servicioProvincia.buscarPorId(idProvinciaActual)
      : pregunta.getProvincia();

    if (provincia == null) {
      request
        .getSession()
        .setAttribute(MENSAJE_RESULTADO, "No se encontro la provincia seleccionada.");
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Partida partidaActualizada = servicioJuego.obtenerPartidaPorId(partidaId);

    ModelMap modelo = new ModelMap();
    modelo.put("partida", partidaActualizada);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put("pregunta", pregunta);
    modelo.put("jugadorActual", partidaActualizada.getJugadorEnTurno());
    modelo.put("idProvincia", provincia.getId());
    modelo.put("opciones", request.getSession().getAttribute("opcionesActuales"));
    modelo.put("provincia", provincia);

    return new ModelAndView("pregunta", modelo);
  }

  @PostMapping("/responder-provincia")
  public ModelAndView responderProvincia(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam("idPregunta") Long idPregunta,
    @RequestParam("respuesta") String respuesta,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession();

    Boolean acerto = servicioJuego.procesarRespuestaYPasarTurno(
      partidaId,
      idProvincia,
      idPregunta,
      respuesta
    );

    if (!acerto) {
      session.setAttribute(MENSAJE_RESULTADO, "Respuesta incorrecta. Fin de tu turno");
      limpiarSesionDisputa(session);
      return new ModelAndView((REDIRECT_JUEGO + partidaId));
    }
    Integer respondidas = (Integer) session.getAttribute(RESPONDIDAS_ATTR);
    Integer requeridas = (Integer) session.getAttribute(REQUERIDAS_ATTR);

    session.setAttribute(RESPONDIDAS_ATTR, respondidas + 1);

    if ((respondidas + 1) == requeridas) {
      if (requeridas == PREGUNTAS_PARA_CONQUISTA) {
        servicioJuego.concretarConquista(partidaId, idProvincia);
        session.setAttribute(
          MENSAJE_RESULTADO,
          "Respondiste las 3 correctas y conquistaste la provincia"
        );
      } else {
        session.setAttribute(MENSAJE_RESULTADO, "¡Respuesta correcta! Provincia colonizada");
      }
      limpiarSesionDisputa(session);
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    } else {
      Pregunta proximaPregunta = servicioPregunta.obtenerPreguntaPorProvincia(idProvincia);
      session.setAttribute("preguntaActual", proximaPregunta);
      session.setAttribute(
        "opcionesActuales",
        servicioPregunta.obtenerOpcionesMezcladas(proximaPregunta)
      );

      return new ModelAndView("redirect:/juego/pregunta-actual?partidaId=" + partidaId);
    }
  }

  private void limpiarSesionDisputa(HttpSession session) {
    session.removeAttribute(REQUERIDAS_ATTR);
    session.removeAttribute(RESPONDIDAS_ATTR);
  }

  @RequestMapping(path = "/juego/tiempo-agotado", method = RequestMethod.POST)
  public ModelAndView tiempoAgotado(@RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId) {
    servicioJuego.forzarSaltoPorTiempo(partidaId);
    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }
}