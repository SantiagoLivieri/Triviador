package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para el lobby e inicio de partida.
 */
@Controller
public class ControladorLobby {

  @GetMapping("/lobby")
  public String mostrarLobby(ModelMap modelo) {
    modelo.put("datosLobby", new DatosLobby());
    return "lobby";
  }
}
