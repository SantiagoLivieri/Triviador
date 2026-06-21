package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorComodin {

  private final ServicioJuego servicioJuego;
  private final ServicioPregunta servicioPregunta;

  private static final String ATRIBUTO_OPCIONES = "opcionesActuales";
  private static final String REDIRECT_PREGUNTA = "redirect:/juego/pregunta-actual?partidaId=";
  private static final String ERROR_COMODIN = "errorComodin";
  private static final String COMODIN_YA_USADO = "comodinUsadoEnEstaPregunta";

  @Autowired
  public ControladorComodin(ServicioJuego servicioJuego, ServicioPregunta servicioPregunta) {
    this.servicioJuego = servicioJuego;
    this.servicioPregunta = servicioPregunta;
  }

  @PostMapping("/juego/usar-comodin")
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
    return new ModelAndView(REDIRECT_PREGUNTA + partidaId);
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

    final Pregunta preguntaActual = (Pregunta) session.getAttribute("preguntaActual");
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
            ATRIBUTO_OPCIONES
          );

          if (opcionesEnPantalla == null || opcionesEnPantalla.size() <= 2) {
            flash.addFlashAttribute(ERROR_COMODIN, "Ya usaste este comodín.");
            break;
          }

          final List<String> opcionesFiltradas = servicioJuego.aplicarComodinEliminarDos(
            idUsuario,
            opcionesEnPantalla,
            preguntaActual
          );

          session.setAttribute(ATRIBUTO_OPCIONES, opcionesFiltradas);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute(
            "mensajeComodin",
            "¡Comodín aplicado! Se eliminaron 2 respuestas incorrectas."
          );
          break;
        }
      case "DOBLE_CHANCE":
        {
          if (Boolean.TRUE.equals((Boolean) session.getAttribute("dobleChanceActivo"))) {
            flash.addFlashAttribute(
              ERROR_COMODIN,
              "Ya tenes la Doble Chance activada para este turno."
            );
            break;
          }

          servicioJuego.aplicarComodinDobleChance(idUsuario);

          session.setAttribute("dobleChanceActivo", true);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute("mensajeComodin", "¡Doble Chance activada!");
          break;
        }
      case "PASAR_PREGUNTA":
        {
          final Set<Long> preguntasYaHechas = recuperarPreguntasYaHechas(session);
          final Long idProvinciaActual = (Long) session.getAttribute("idProvinciaActual");

          final Pregunta nuevaPregunta = servicioJuego.aplicarComodinPasarPregunta(
            idUsuario,
            preguntaActual,
            idProvinciaActual,
            preguntasYaHechas
          );

          final List<String> nuevasOpciones = servicioPregunta.obtenerOpcionesMezcladas(
            nuevaPregunta
          );

          session.setAttribute("preguntaActual", nuevaPregunta);
          session.setAttribute(ATRIBUTO_OPCIONES, nuevasOpciones);
          session.setAttribute("preguntasYaHechas", preguntasYaHechas);
          session.setAttribute(COMODIN_YA_USADO, true);

          flash.addFlashAttribute("mensajeComodin", "¡Pregunta saltada exitosamente!");
          break;
        }
      default:
        flash.addFlashAttribute(ERROR_COMODIN, "El tipo de comodín no es reconocido.");
        break;
    }
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
