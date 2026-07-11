package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.controladores.clasesAuxiliares.EstadoDePartida;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.MotivoReportePregunta;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/juego")
public class ControladorJuego {

  private final ServicioJuego servicioJuego;
  private final ServicioRespuestaPartida servicioRespuestaPartida;

  private static final String ATRIBUTO_PARTIDA_ID = "partidaId";

  private static final String REDIRECT_JUEGO = "redirect:/juego/partida/";

  private static final String REDIRECT_HOME = "redirect:/home";
  private static final String REDIRECT_LOGIN = "redirect:/login";
  private static final String MENSAJE_RESULTADO = "mensajeResultado";

  private static final String USUARIO_LOGUEADO = "usuarioLogueado";
  public static final String REQUERIDAS_ATTR = "preguntasRequeridas";
  public static final String RESPONDIDAS_ATTR = "preguntasRespondidasExito";

  @Autowired
  public ControladorJuego(
    ServicioJuego servicioJuego,
    ServicioRespuestaPartida servicioRespuestaPartida
  ) {
    this.servicioJuego = servicioJuego;
    this.servicioRespuestaPartida = servicioRespuestaPartida;
  }

  @PostMapping("/iniciar-partida")
  public ModelAndView iniciarPartida(
    @ModelAttribute("datosLobby") DatosLobby datosLobby,
    HttpSession session
  ) {
    Usuario usuarioAnfitrion = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    Long partidaId = servicioJuego.inicializarPartida(datosLobby, usuarioAnfitrion);

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @PostMapping("/partida/{partidaId}/abandonar")
  public ModelAndView abandonarPartidaLocal(
    @PathVariable Long partidaId,
    HttpSession session,
    RedirectAttributes flash
  ) {
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    if (usuarioId == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    try {
      servicioJuego.abandonarPartidaLocal(partidaId, usuarioId);

      Usuario usuarioActualizado = servicioJuego.obtenerUsuarioPorId(usuarioId);

      session.setAttribute(USUARIO_LOGUEADO, usuarioActualizado);

      limpiarDatosDePartida(session);

      flash.addFlashAttribute("mensajeExito", "Abandonaste la partida. Se descontaron 20 XP.");

      return new ModelAndView(REDIRECT_HOME);
    } catch (IllegalArgumentException | IllegalStateException e) {
      flash.addFlashAttribute("mensajeError", e.getMessage());

      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }
  }

  @GetMapping("/partida/{id}")
  public ModelAndView mostrarJuego(@PathVariable("id") Long partidaId, HttpSession session) {
    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    if (partida == null) {
      return new ModelAndView(REDIRECT_HOME);
    }

    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    if (!usuarioParticipaEnPartida(partida, usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    if (partida.getJugadorEnTurno() == null && !partida.getJugadores().isEmpty()) {
      partida.setJugadorEnTurno(partida.getJugadores().get(0));
    }

    ModelMap modelo = new ModelMap();

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

  @RequestMapping(path = "/tiempo-agotado", method = RequestMethod.POST)
  public ModelAndView tiempoAgotado(
    @RequestParam(ATRIBUTO_PARTIDA_ID) Long partidaId,
    HttpSession session
  ) {
    servicioJuego.forzarSaltoPorTiempo(partidaId);
    Long usuarioId = (Long) session.getAttribute("usuarioId");

    if (servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId)) {
      return new ModelAndView("redirect:/juego/partida/resultados/" + partidaId);
    }

    return new ModelAndView(REDIRECT_JUEGO + partidaId);
  }

  @GetMapping("/partida/resultados/{partidaId}")
  public ModelAndView mostrarResultados(
    @PathVariable("partidaId") Long partidaId,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    if (partida == null) {
      return new ModelAndView(REDIRECT_HOME);
    }

    if (partida.getEstadoDePartida() == EstadoDePartida.ABANDONADA) {
      return new ModelAndView(REDIRECT_HOME);
    }

    if (!partida.estaFinalizada()) {
      return new ModelAndView(REDIRECT_JUEGO + partidaId);
    }

    if (!usuarioParticipaEnPartida(partida, usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    List<Jugador> ranking = partida.obtenerRanking();

    if (ranking.isEmpty()) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();

    modelo.put("ranking", ranking);
    modelo.put("ganador", ranking.get(0));
    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);

    return new ModelAndView("resultados", modelo);
  }

  @GetMapping("/partida/resultados/{partidaId}/preguntas")
  public ModelAndView mostrarHistorialPreguntas(
    @PathVariable("partidaId") Long partidaId,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Partida partida = servicioJuego.obtenerPartidaPorId(partidaId);

    if (partida == null) {
      return new ModelAndView(REDIRECT_HOME);
    }

    if (partida.getEstadoDePartida() != EstadoDePartida.FINALIZADA) {
      return new ModelAndView(REDIRECT_HOME);
    }

    if (!usuarioParticipaEnPartida(partida, usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();

    modelo.put(ATRIBUTO_PARTIDA_ID, partidaId);

    modelo.put(
      "respuestasPartida",
      servicioRespuestaPartida.buscarPorPartidaYUsuario(partidaId, usuario.getId())
    );

    modelo.put("motivosReporte", MotivoReportePregunta.values());

    return new ModelAndView("historial-preguntas", modelo);
  }

  private boolean usuarioParticipaEnPartida(Partida partida, Usuario usuario) {
    for (Jugador jugador : partida.getJugadores()) {
      Usuario usuarioJugador = jugador.getUsuario();

      if (usuarioJugador != null && usuarioJugador.getId().equals(usuario.getId())) {
        return true;
      }
    }
    return false;
  }

  private void limpiarDatosDePartida(HttpSession session) {
    session.removeAttribute("preguntasRequeridas");
    session.removeAttribute("preguntasRespondidasExito");
    session.removeAttribute("preguntaActual");
    session.removeAttribute("opcionesActuales");
    session.removeAttribute("idProvinciaActual");
    session.removeAttribute("comodinUsadoEnEstaPregunta");
    session.removeAttribute("dobleChanceActivo");
    session.removeAttribute("preguntasYaHechas");
    session.removeAttribute("mensajeResultado");
  }
}
