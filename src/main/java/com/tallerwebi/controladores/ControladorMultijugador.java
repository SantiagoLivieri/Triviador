package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPartida;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
public class ControladorMultijugador {

  private final ServicioPartida servicioPartida;
  private final ServicioJuego servicioJuego;

    public ControladorMultijugador(ServicioPartida servicioPartida, ServicioJuego servicioJuego) {
        this.servicioPartida = servicioPartida;
        this.servicioJuego = servicioJuego;
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

  @PostMapping("/disputa/responder")
  public ModelAndView responderMultijugador(
          @RequestParam("partidaId") Long partidaId,
          @RequestParam("idProvincia") Long idProvincia,
          @RequestParam("respondidas") Integer respondidas,
          @RequestParam("requeridas") Integer requeridas,
          HttpSession session
  ) {
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

    try {
      servicioJuego.validarTurnoMultijugador(partidaId, usuarioLogueado.getId());

      String mensaje = servicioJuego.evaluarAcierto(partidaId, idProvincia, respondidas, requeridas);

      if (mensaje != null) {
        session.setAttribute("mensajeResultado", mensaje);
      }

      return new ModelAndView("redirect:/multijugador/partida/" + partidaId);

    } catch (TurnoInvalidoException e) {
      // 5. Si no es su turno, bloqueamos y enviamos un error
      session.setAttribute("mensajeResultado", "Error: ¡No es tu turno de responder!");
      return new ModelAndView("redirect:/multijugador/partida/" + partidaId);
    }
  }

}
