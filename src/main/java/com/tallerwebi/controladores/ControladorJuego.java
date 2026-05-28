package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.servicios.ServicioJuego;
import javax.servlet.http.HttpServletRequest;
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
  private static final String REDIRECT_JUEGO = "redirect:/juego?id=";
  private static final String MENSAJE_RESULTADO = "mensajeResultado";
  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";

  @Autowired
  public ControladorJuego(ServicioJuego servicioJuego) {
    this.servicioJuego = servicioJuego;
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
    modelo.put("provincias", servicioJuego.obtenerProvincias());
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
    Pregunta pregunta = servicioJuego.obtenerPreguntaPorProvincia(idProvincia);
    if (pregunta == null) {
      request.getSession().setAttribute(MENSAJE_RESULTADO, "No hay preguntas cargadas.");
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    try {
      servicioJuego.procesarJugada(partidaId, partida.getJugadorEnTurno().getId(), idProvincia);
    } catch (Exception e) {
      request.getSession().setAttribute(MENSAJE_RESULTADO, e.getMessage());
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    request.getSession().setAttribute("preguntaActual", pregunta);
    request
      .getSession()
      .setAttribute("opcionesActuales", servicioJuego.obtenerOpcionesMezcladas(pregunta));
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

    Partida partidaActualizada = servicioJuego.obtenerPartidaPorId(partidaId);

    ModelMap modelo = new ModelMap();
    modelo.put("partida", partidaActualizada);
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);
    modelo.put("pregunta", pregunta);
    modelo.put("jugadorActual", partidaActualizada.getJugadorEnTurno());
    modelo.put("idProvincia", request.getSession().getAttribute("idProvinciaActual"));
    modelo.put("opciones", request.getSession().getAttribute("opcionesActuales"));

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
    Boolean acerto = servicioJuego.procesarRespuestaYPasarTurno(
      partidaId,
      idProvincia,
      idPregunta,
      respuesta
    );

    if (acerto) {
      request
        .getSession()
        .setAttribute(MENSAJE_RESULTADO, "¡Respuesta correcta! Provincia conquistada.");
    } else {
      request
        .getSession()
        .setAttribute(MENSAJE_RESULTADO, "Respuesta incorrecta. Fin de tu turno.");
    }

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @RequestMapping(path = "/juego/tiempo-agotado", method = RequestMethod.POST)
  public ModelAndView tiempoAgotado(@RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId) {
    servicioJuego.forzarSaltoPorTiempo(partidaId);
    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }
}
