package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosReportePregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/juego/partida")
public class ControladorReportePregunta {

  private static final String REDIRECT_HISTORIAL = "redirect:/juego/partida/resultados/";

  private final ServicioRespuestaPartida servicioRespuestaPartida;

  @Autowired
  public ControladorReportePregunta(ServicioRespuestaPartida servicioRespuestaPartida) {
    this.servicioRespuestaPartida = servicioRespuestaPartida;
  }

  @PostMapping("/{partidaId}/reportar-pregunta")
  public ModelAndView reportarPregunta(
    @PathVariable("partidaId") Long partidaId,
    @ModelAttribute DatosReportePregunta datosReporte,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    procesarReporte(partidaId, datosReporte, usuario, flash);

    return new ModelAndView(REDIRECT_HISTORIAL + partidaId + "/preguntas");
  }

  private void procesarReporte(
    Long partidaId,
    DatosReportePregunta datosReporte,
    Usuario usuario,
    RedirectAttributes flash
  ) {
    try {
      servicioRespuestaPartida.reportarPregunta(
        partidaId,
        datosReporte.getRespuestaPartidaId(),
        usuario.getId(),
        datosReporte.getMotivo(),
        datosReporte.getComentario()
      );

      flash.addFlashAttribute("mensajeExito", "La pregunta fue reportada correctamente.");
    } catch (IllegalArgumentException | IllegalStateException exception) {
      flash.addFlashAttribute("mensajeError", exception.getMessage());
    }
  }
}
