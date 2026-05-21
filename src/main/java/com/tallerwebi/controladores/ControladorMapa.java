package com.tallerwebi.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorMapa {

  @GetMapping("/mapa")
  public String mapa() {
    return "mapa";
  }
}
