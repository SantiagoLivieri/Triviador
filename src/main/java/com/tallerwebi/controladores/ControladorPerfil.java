package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioHistorial;
import com.tallerwebi.servicios.ServicioUsuario;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPerfil {

  private final ServicioUsuario servicioUsuario;
  private final ServicioHistorial servicioHistorial;

  @Autowired
  public ControladorPerfil(ServicioUsuario servicioUsuario, ServicioHistorial servicioHistorial) {
    this.servicioUsuario = servicioUsuario;
    this.servicioHistorial = servicioHistorial;
  }

  @RequestMapping("/perfil")
  public ModelAndView verPerfil(HttpSession session) {
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    Usuario usuario = servicioUsuario.buscarUsuarioPorId(usuarioId);

    ModelMap modelMap = new ModelMap();
    modelMap.put("usuario", usuario);
    modelMap.put("historial", servicioHistorial.buscarHistorialPorUsuario(usuarioId));

    return new ModelAndView("perfil", modelMap);
  }

  @RequestMapping(path = "/perfil/guardar", method = RequestMethod.POST)
  public ModelAndView guardarPerfil(String nombre, String nombreJugador, HttpSession session) {
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    servicioUsuario.actualizarPerfil(usuarioId, nombre, nombreJugador);

    Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuarioId);
    session.setAttribute("usuarioLogueado", usuarioActualizado);

    return new ModelAndView("redirect:/perfil");
  }
}
