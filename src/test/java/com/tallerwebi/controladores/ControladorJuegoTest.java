package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.controladores.clasesAuxiliares.EstadoDePartida;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.RespuestaPartida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorJuegoTest {

  private ServicioJuego servicioJuegoMock;
  private ServicioRespuestaPartida servicioRespuestaPartidaMock;
  private ControladorJuego controladorJuego;

  @BeforeEach
  public void init() {
    /*
     * El controlador ahora depende de dos servicios:
     * ServicioJuego mantiene el flujo existente y
     * ServicioRespuestaPartida carga el historial final.
     */
    this.servicioJuegoMock = mock(ServicioJuego.class);

    this.servicioRespuestaPartidaMock = mock(ServicioRespuestaPartida.class);

    this.controladorJuego =
      new ControladorJuego(this.servicioJuegoMock, this.servicioRespuestaPartidaMock);
  }

  @Test
  public void iniciarPartidaDeberiaRedirigirAlJuegoConElIdDeLaPartida() {
    HttpSession sessionMock = mock(HttpSession.class);

    Usuario usuarioMock = mock(Usuario.class);

    DatosLobby datosLobbyMock = mock(DatosLobby.class);

    when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

    when(servicioJuegoMock.inicializarPartida(datosLobbyMock, usuarioMock)).thenReturn(1L);

    ModelAndView resultado = controladorJuego.iniciarPartida(datosLobbyMock, sessionMock);

    assertEquals("redirect:/juego/partida/1", resultado.getViewName());

    verify(servicioJuegoMock).inicializarPartida(datosLobbyMock, usuarioMock);
  }

  @Test
  public void mostrarJuegoSinMensajeResultadoDeberiaCargarModelo() {
    HttpSession sessionMock = mock(HttpSession.class);

    Partida partidaMock = mock(Partida.class);

    Jugador jugadorMock = mock(Jugador.class);

    Usuario usuarioMock = mock(Usuario.class);

    configurarUsuarioParticipante(sessionMock, partidaMock, jugadorMock, usuarioMock);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getJugadorEnTurno()).thenReturn(jugadorMock);

    when(partidaMock.obtenerMapaDeColoresPorJugador()).thenReturn(new HashMap<>());

    when(servicioJuegoMock.obtenerProvinciasDelTablero()).thenReturn(new ArrayList<>());

    when(sessionMock.getAttribute("mensajeResultado")).thenReturn(null);

    ModelAndView resultado = controladorJuego.mostrarJuego(1L, sessionMock);

    assertEquals("juego", resultado.getViewName());

    assertSame(partidaMock, resultado.getModel().get("partida"));
  }

  @Test
  public void mostrarJuegoConMensajeResultadoDeberiaAgregarMensajeAlModelo() {
    HttpSession sessionMock = mock(HttpSession.class);

    Partida partidaMock = mock(Partida.class);

    Jugador jugadorMock = mock(Jugador.class);

    Usuario usuarioMock = mock(Usuario.class);

    configurarUsuarioParticipante(sessionMock, partidaMock, jugadorMock, usuarioMock);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getJugadorEnTurno()).thenReturn(jugadorMock);

    when(partidaMock.obtenerMapaDeColoresPorJugador()).thenReturn(new HashMap<>());

    when(servicioJuegoMock.obtenerProvinciasDelTablero()).thenReturn(new ArrayList<>());

    when(sessionMock.getAttribute("mensajeResultado")).thenReturn("Correcto");

    ModelAndView resultado = controladorJuego.mostrarJuego(1L, sessionMock);

    assertEquals("juego", resultado.getViewName());

    assertEquals("Correcto", resultado.getModel().get("mensajeResultado"));

    verify(sessionMock).removeAttribute("mensajeResultado");
  }

  @Test
  public void mostrarMapaDeberiaRetornarVistaMapa() {
    String vista = controladorJuego.mostrarMapa();

    assertEquals("mapa", vista);
  }

  @Test
  public void tiempoAgotadoDeberiaRedirigirAResultadosCuandoLaPartidaFinaliza() {
    HttpSession sessionMock = mock(HttpSession.class);

    when(sessionMock.getAttribute("usuarioId")).thenReturn(10L);

    when(servicioJuegoMock.evaluarYFinalizarPartida(1L, 10L)).thenReturn(true);

    ModelAndView resultado = controladorJuego.tiempoAgotado(1L, sessionMock);

    assertEquals("redirect:/juego/partida/resultados/1", resultado.getViewName());

    verify(servicioJuegoMock).forzarSaltoPorTiempo(1L);
  }

  @Test
  public void tiempoAgotadoDeberiaRedirigirAlJuegoCuandoLaPartidaContinua() {
    HttpSession sessionMock = mock(HttpSession.class);

    when(sessionMock.getAttribute("usuarioId")).thenReturn(10L);

    when(servicioJuegoMock.evaluarYFinalizarPartida(1L, 10L)).thenReturn(false);

    ModelAndView resultado = controladorJuego.tiempoAgotado(1L, sessionMock);

    assertEquals("redirect:/juego/partida/1", resultado.getViewName());

    verify(servicioJuegoMock).forzarSaltoPorTiempo(1L);
  }

  @Test
  public void mostrarResultadosDeberiaRedirigirAlJuegoCuandoLaPartidaNoFinalizo() {
    HttpSession sessionMock = mock(HttpSession.class);

    Partida partidaMock = mock(Partida.class);

    Usuario usuarioMock = mock(Usuario.class);

    when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.estaFinalizada()).thenReturn(false);

    ModelAndView resultado = controladorJuego.mostrarResultados(1L, sessionMock);

    assertEquals("redirect:/juego/partida/1", resultado.getViewName());
  }

  @Test
  public void mostrarResultadosDeberiaMostrarRankingEHistorialCuandoLaPartidaFinalizo() {
    HttpSession sessionMock = mock(HttpSession.class);

    Partida partidaMock = mock(Partida.class);

    Jugador jugadorUno = mock(Jugador.class);

    Jugador jugadorDos = mock(Jugador.class);

    Usuario usuarioMock = mock(Usuario.class);

    RespuestaPartida respuestaMock = mock(RespuestaPartida.class);

    List<Jugador> ranking = List.of(jugadorUno, jugadorDos);

    List<RespuestaPartida> respuestas = List.of(respuestaMock);

    /*
     * El usuario debe estar autenticado y participar en la partida,
     * porque el controlador valida ambas condiciones antes de mostrar
     * las respuestas correctas.
     */
    configurarUsuarioParticipante(sessionMock, partidaMock, jugadorUno, usuarioMock);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getEstadoDePartida()).thenReturn(EstadoDePartida.FINALIZADA);

    when(partidaMock.estaFinalizada()).thenReturn(true);

    when(partidaMock.obtenerRanking()).thenReturn(ranking);

    when(servicioRespuestaPartidaMock.buscarPorPartidaYUsuario(1L, 10L)).thenReturn(respuestas);

    ModelAndView resultado = controladorJuego.mostrarResultados(1L, sessionMock);

    assertEquals("resultados", resultado.getViewName());

    assertEquals(ranking, resultado.getModel().get("ranking"));

    assertSame(jugadorUno, resultado.getModel().get("ganador"));

    assertEquals(respuestas, resultado.getModel().get("respuestasPartida"));

    assertEquals(1L, resultado.getModel().get("partidaId"));

    verify(servicioRespuestaPartidaMock).buscarPorPartidaYUsuario(1L, 10L);
  }

  @Test
  public void mostrarResultadosDeberiaRedirigirAlHomeCuandoLaPartidaFueAbandonada() {
    HttpSession sessionMock = mock(HttpSession.class);

    Partida partidaMock = mock(Partida.class);

    Usuario usuarioMock = mock(Usuario.class);

    when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getEstadoDePartida()).thenReturn(EstadoDePartida.ABANDONADA);

    ModelAndView resultado = controladorJuego.mostrarResultados(1L, sessionMock);

    assertEquals("redirect:/home", resultado.getViewName());
  }

  /*
   * Helper para evitar repetir en cada test la configuración que
   * demuestra que el usuario autenticado pertenece a la partida.
   */
  private void configurarUsuarioParticipante(
    HttpSession sessionMock,
    Partida partidaMock,
    Jugador jugadorMock,
    Usuario usuarioMock
  ) {
    when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

    when(usuarioMock.getId()).thenReturn(10L);

    when(jugadorMock.getUsuario()).thenReturn(usuarioMock);

    when(partidaMock.getJugadores()).thenReturn(List.of(jugadorMock));
  }
}
