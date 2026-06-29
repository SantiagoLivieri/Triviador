package com.tallerwebi.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLobby {

  @GetMapping("/lobby")
  public ModelAndView mostrarLobbyLocal() {
    return new ModelAndView("lobby");
  }

  @GetMapping("/lobby-online")
  public ModelAndView mostrarLobbyOnline(HttpSession session) {
    return new ModelAndView("lobby-online");
  }
}
