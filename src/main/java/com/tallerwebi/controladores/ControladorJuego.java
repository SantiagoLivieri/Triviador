package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorJuego {

  private final ServicioJuego servicioJuego;

  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";

  private static final String REDIRECT_JUEGO = "redirect:/juego?id=";

  private static final String MENSAJE_RESULTADO = "mensajeResultado";
  public static final String REQUERIDAS_ATTR = "preguntasRequeridas";
  public static final String RESPONDIDAS_ATTR = "preguntasRespondidasExito";

  @Autowired
  public ControladorJuego(ServicioJuego servicioJuego) {
    this.servicioJuego = servicioJuego;
  }

  @PostMapping("/iniciar-partida")
  public ModelAndView iniciarPartida(
    @ModelAttribute("datosLobby") DatosLobby datosLobby,
    HttpSession session
  ) {
    Usuario usuarioAnfitrion = (Usuario) session.getAttribute("usuarioLogueado");
    Long partidaId = servicioJuego.inicializarPartida(datosLobby, usuarioAnfitrion);

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/juego")
  public ModelAndView mostrarJuego(@RequestParam("id") Long partidaId, HttpSession session) {
    ModelMap modelo = new ModelMap();
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    modelo.put("coloresPorJugador", partida.obtenerMapaDeColoresPorJugador());
    modelo.put("partida", partida);
    modelo.put("jugadores", partida.getJugadores());
    modelo.put("provincias", servicioJuego.obtenerProvinciasDelTablero());
    modelo.put("jugadorActual", partida.getJugadorEnTurno());

    String mensaje = (String) session.getAttribute(MENSAJE_RESULTADO);
    if (mensaje != null) {
      modelo.put(MENSAJE_RESULTADO, mensaje);
      session.removeAttribute(MENSAJE_RESULTADO);
    }

    return new ModelAndView("juego", modelo);
  }

  @GetMapping("/mapa")
  public String mostrarMapa() {
    return "mapa";
  }

  @RequestMapping(path = "/juego/tiempo-agotado", method = RequestMethod.POST)
  public ModelAndView tiempoAgotado(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    servicioJuego.forzarSaltoPorTiempo(partidaId);
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    if (servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)) {
      return new ModelAndView("redirect:/partida/resultados/" + partidaId);
    }

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/partida/resultados/{partidaId}")
  public ModelAndView mostrarResultados(@PathVariable Long partidaId) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);
    if (!partida.estaFinalizada()) {
      return new ModelAndView("redirect:/partida/tablero/" + partidaId);
    }

    ModelMap modelo = new ModelMap();
    List<Jugador> ranking = partida.obtenerRanking();

    modelo.put("ranking", ranking);
    modelo.put("ganador", ranking.get(0));

    return new ModelAndView("resultados", modelo);
  }
}
