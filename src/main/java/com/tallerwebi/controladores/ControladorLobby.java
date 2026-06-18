package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlador para el lobby e inicio de partida.
 */
@Controller
public class ControladorLobby {

  @GetMapping("/lobby")
  public ModelAndView mostrarLobby(HttpSession session) {
    ModelMap modelo = new ModelMap();
    modelo.put("datosLobby", new DatosLobby());

    Usuario anfitrion = (Usuario) session.getAttribute("usuarioLogueado");
    modelo.put("usuario", anfitrion);

    return new ModelAndView("lobby", modelo);
  }
}
