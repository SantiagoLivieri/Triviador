package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLobby {

  @GetMapping("/lobby")
  public ModelAndView mostrarLobbyLocal(HttpSession session) {
    ModelAndView model = new ModelAndView("lobby");

    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    model.addObject("usuario", usuario);

    model.addObject("datosLobby", new DatosLobby());

    return model;
  }

  @GetMapping("/lobby-online")
  public ModelAndView mostrarLobbyOnline(HttpSession session) {
    return new ModelAndView("lobby-online");
  }
}
