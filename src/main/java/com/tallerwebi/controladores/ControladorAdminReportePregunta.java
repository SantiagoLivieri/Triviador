package com.tallerwebi.controladores;

import com.tallerwebi.entidades.EstadoReportePregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorAdminReportePregunta {

  private static final String ROL_ADMIN = "ADMIN";

  private static final String USUARIO_LOGUEADO = "usuarioLogueado";

  private static final String REDIRECT_HOME = "redirect:/home";

  private static final String REDIRECT_REPORTES = "redirect:/admin/reportes-pregunta";

  private static final String VISTA_REPORTES = "admin-reportes-pregunta";

  private final ServicioRespuestaPartida servicioRespuestaPartida;

  @Autowired
  public ControladorAdminReportePregunta(ServicioRespuestaPartida servicioRespuestaPartida) {
    this.servicioRespuestaPartida = servicioRespuestaPartida;
  }

  @GetMapping("/admin/reportes-pregunta")
  public ModelAndView mostrarReportes(HttpSession session) {
    Usuario usuario = obtenerUsuario(session);

    if (!esAdmin(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();

    modelo.put("reportes", servicioRespuestaPartida.obtenerReportesPendientes());

    modelo.put("estadosDisponibles", EstadoReportePregunta.values());

    return new ModelAndView(VISTA_REPORTES, modelo);
  }

  @PostMapping("/admin/reportes-pregunta/estado")
  public ModelAndView cambiarEstado(
    @RequestParam("id") Long reporteId,
    @RequestParam("estado") EstadoReportePregunta nuevoEstado,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Usuario usuario = obtenerUsuario(session);

    if (!esAdmin(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    try {
      servicioRespuestaPartida.cambiarEstadoReporte(reporteId, nuevoEstado, usuario);

      flash.addFlashAttribute("mensajeExito", "El reporte fue actualizado.");
    } catch (IllegalArgumentException | IllegalStateException exception) {
      flash.addFlashAttribute("mensajeError", exception.getMessage());
    }

    return new ModelAndView(REDIRECT_REPORTES);
  }

  private Usuario obtenerUsuario(HttpSession session) {
    return (Usuario) session.getAttribute(USUARIO_LOGUEADO);
  }

  private boolean esAdmin(Usuario usuario) {
    return (
      usuario != null &&
      usuario.getRol() != null &&
      "ADMIN".equals(usuario.getRol().getDescripcion())
    );
  }
}
