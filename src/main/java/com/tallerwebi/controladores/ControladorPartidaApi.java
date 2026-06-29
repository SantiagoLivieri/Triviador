package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioPartida;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ControladorPartidaApi {

  private final ServicioPartida servicioPartida;

  public ControladorPartidaApi(ServicioPartida servicioPartida) {
    this.servicioPartida = servicioPartida;
  }

  @PostMapping("/buscar-partida")
  public Map<String, Object> buscarPartida(HttpSession httpSession) {
    Usuario usuario = (Usuario) httpSession.getAttribute("usuarioLogueado");

    Partida partida = servicioPartida.buscarOCrearPartida(usuario);
    Map<String, Object> response = new HashMap<>();
    response.put("id", partida.getId());
    return response;
  }

  @GetMapping("/estado-partida/{id}")
  public Map<String, Object> estadoPartida(@PathVariable Long id) {
    int cantidad = servicioPartida.contarJugadoresEnPartida(id);
    Map<String, Object> response = new HashMap<>();
    response.put("cantidad", cantidad);
    return response;
  }
}
