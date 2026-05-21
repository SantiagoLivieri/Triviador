package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.servicios.ServicioJuego;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JuegoController {

  private static final String TURNO_ACTUAL = "turnoActual";
  private static final String MENSAJE_RESULTADO = "mensajeResultado";

  private final ServicioJuego servicioJuego;

  @Autowired
  public JuegoController(ServicioJuego servicioJuego) {
    this.servicioJuego = servicioJuego;
  }

  @PostMapping("/iniciar-partida")
  public String iniciarPartida(
    @ModelAttribute("datosLobby") DatosLobby datosLobby,
    HttpSession session
  ) {
    servicioJuego.inicializarPartida(datosLobby);
    session.setAttribute(TURNO_ACTUAL, 0);

    return "redirect:/juego";
  }

  @GetMapping("/juego")
  public String mostrarJuego(ModelMap modelo, HttpSession session) {
    Integer turnoActual = obtenerTurnoActual(session);

    modelo.put("jugadores", servicioJuego.obtenerJugadores());
    modelo.put("provincias", servicioJuego.obtenerProvincias());
    modelo.put("jugadorActual", servicioJuego.obtenerJugadorDelTurno(turnoActual));

    Object mensajeResultado = session.getAttribute(MENSAJE_RESULTADO);

    if (mensajeResultado != null) {
      modelo.put(MENSAJE_RESULTADO, mensajeResultado);
      session.removeAttribute(MENSAJE_RESULTADO);
    }

    return "juego";
  }

  @PostMapping("/seleccionar-provincia")
  public String seleccionarProvincia(
    @RequestParam("idProvincia") Long idProvincia,
    ModelMap modelo,
    HttpSession session
  ) {
    Pregunta pregunta = servicioJuego.obtenerPreguntaAleatoria();

    if (pregunta == null) {
      session.setAttribute(MENSAJE_RESULTADO, "No hay preguntas cargadas.");
      return "redirect:/juego";
    }

    Integer turnoActual = obtenerTurnoActual(session);

    modelo.put("idProvincia", idProvincia);
    modelo.put("pregunta", pregunta);
    modelo.put("opciones", obtenerOpcionesMezcladas(pregunta));
    modelo.put("jugadorActual", servicioJuego.obtenerJugadorDelTurno(turnoActual));

    return "pregunta";
  }

  @PostMapping("/responder-provincia")
  public String responderProvincia(
    @RequestParam("idProvincia") Long idProvincia,
    @RequestParam("idPregunta") Long idPregunta,
    @RequestParam("respuesta") String respuesta,
    HttpSession session
  ) {
    Integer turnoActual = obtenerTurnoActual(session);

    Boolean acerto = servicioJuego.responderPregunta(
      idProvincia,
      idPregunta,
      respuesta,
      turnoActual
    );

    if (acerto) {
      session.setAttribute(MENSAJE_RESULTADO, "Respuesta correcta. Provincia conquistada.");
    } else {
      session.setAttribute(
        MENSAJE_RESULTADO,
        "Respuesta incorrecta. No conquistaste la provincia."
      );
    }

    avanzarTurno(session);

    return "redirect:/juego";
  }

  private Integer obtenerTurnoActual(HttpSession session) {
    Object turno = session.getAttribute(TURNO_ACTUAL);

    if (turno == null) {
      session.setAttribute(TURNO_ACTUAL, 0);
      return 0;
    }

    return (Integer) turno;
  }

  private void avanzarTurno(HttpSession session) {
    Integer cantidadJugadores = servicioJuego.obtenerJugadores().size();

    if (cantidadJugadores == 0) {
      session.setAttribute(TURNO_ACTUAL, 0);
      return;
    }

    Integer turnoActual = obtenerTurnoActual(session);
    Integer proximoTurno = (turnoActual + 1) % cantidadJugadores;

    session.setAttribute(TURNO_ACTUAL, proximoTurno);
  }

  private List<String> obtenerOpcionesMezcladas(Pregunta pregunta) {
    List<String> opciones = new ArrayList<>();

    opciones.add(pregunta.getRespuestaCorrecta());
    opciones.add(pregunta.getOpcionIncorrectaUno());
    opciones.add(pregunta.getOpcionIncorrectaDos());
    opciones.add(pregunta.getOpcionIncorrectaTres());

    Collections.shuffle(opciones);

    return opciones;
  }
}
