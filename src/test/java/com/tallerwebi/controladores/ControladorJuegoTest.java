package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorJuegoTest {

  private ServicioJuego servicioJuegoMock;
  private ControladorJuego controladorJuego;

  @BeforeEach
  public void init() {
    this.servicioJuegoMock = mock(ServicioJuego.class);
    this.controladorJuego = new ControladorJuego(this.servicioJuegoMock);
  }

  @Test
  public void iniciarPartidaDeberiaRedirigirAlJuegoConElIdDeLaPartida() {
    HttpSession sessionMock = mock(HttpSession.class);
    Usuario usuarioMock = mock(Usuario.class);
    DatosLobby datosLobbyMock = mock(DatosLobby.class);

    when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

    when(servicioJuegoMock.inicializarPartida(datosLobbyMock, usuarioMock)).thenReturn(1L);

    ModelAndView resultado = controladorJuego.iniciarPartida(datosLobbyMock, sessionMock);

    assertEquals("redirect:/juego?id=1", resultado.getViewName());
    verify(servicioJuegoMock).inicializarPartida(datosLobbyMock, usuarioMock);
  }

  @Test
  public void mostrarJuegoSinMensajeResultadoDeberiaCargarModelo() {
    HttpSession sessionMock = mock(HttpSession.class);
    Partida partidaMock = mock(Partida.class);
    Jugador jugadorMock = mock(Jugador.class);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getJugadores()).thenReturn(new ArrayList<>());

    when(partidaMock.getJugadorEnTurno()).thenReturn(jugadorMock);

    when(partidaMock.obtenerMapaDeColoresPorJugador()).thenReturn(new HashMap<>());

    when(servicioJuegoMock.obtenerProvinciasDelTablero()).thenReturn(new ArrayList<>());

    when(sessionMock.getAttribute("mensajeResultado")).thenReturn(null);

    ModelAndView resultado = controladorJuego.mostrarJuego(1L, sessionMock);

    assertEquals("juego", resultado.getViewName());
  }

  @Test
  public void mostrarJuegoConMensajeResultadoDeberiaAgregarMensajeAlModelo() {
    HttpSession sessionMock = mock(HttpSession.class);
    Partida partidaMock = mock(Partida.class);
    Jugador jugadorMock = mock(Jugador.class);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.getJugadores()).thenReturn(new ArrayList<>());

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

    assertEquals("redirect:/partida/resultados/1", resultado.getViewName());
    verify(servicioJuegoMock).forzarSaltoPorTiempo(1L);
  }

  @Test
  public void tiempoAgotadoDeberiaRedirigirAlJuegoCuandoLaPartidaContinua() {
    HttpSession sessionMock = mock(HttpSession.class);

    when(sessionMock.getAttribute("usuarioId")).thenReturn(10L);

    when(servicioJuegoMock.evaluarYFinalizarPartida(1L, 10L)).thenReturn(false);

    ModelAndView resultado = controladorJuego.tiempoAgotado(1L, sessionMock);

    assertEquals("redirect:/juego?id=1", resultado.getViewName());
    verify(servicioJuegoMock).forzarSaltoPorTiempo(1L);
  }

  @Test
  public void mostrarResultadosDeberiaRedirigirAlTableroCuandoLaPartidaNoFinalizo() {
    Partida partidaMock = mock(Partida.class);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.estaFinalizada()).thenReturn(false);

    ModelAndView resultado = controladorJuego.mostrarResultados(1L);

    assertEquals("redirect:/partida/tablero/1", resultado.getViewName());
  }

  @Test
  public void mostrarResultadosDeberiaMostrarResultadoCuandoLaPartidaFinalizo() {
    Partida partidaMock = mock(Partida.class);
    Jugador jugador1 = mock(Jugador.class);
    Jugador jugador2 = mock(Jugador.class);

    List<Jugador> ranking = new ArrayList<>();
    ranking.add(jugador1);
    ranking.add(jugador2);

    when(servicioJuegoMock.obtenerPartidaPorId(1L)).thenReturn(partidaMock);

    when(partidaMock.estaFinalizada()).thenReturn(true);

    when(partidaMock.obtenerRanking()).thenReturn(ranking);

    ModelAndView resultado = controladorJuego.mostrarResultados(1L);

    assertEquals(("resultados"), resultado.getViewName());
    assertEquals(ranking, resultado.getModel().get("ranking"));
    assertEquals(jugador1, resultado.getModel().get("ganador"));
  }
}
