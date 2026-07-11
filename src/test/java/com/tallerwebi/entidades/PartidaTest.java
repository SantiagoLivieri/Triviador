package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class PartidaTest {

  @Test
  public void alObtenerJugadoresSiLaListaEsNullDebeRetornarListaVacia() {
    Partida partida = new Partida();

    List<Jugador> jugadores = partida.getJugadores();

    assertTrue(jugadores.isEmpty());
  }

  @Test
  public void alObtenerJugadoresDebeEliminarDuplicadosPorId() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorUnoDuplicado = crearJugador(1L, "Santi duplicado", 20, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 30, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorUnoDuplicado, jugadorDos));

    List<Jugador> jugadores = partida.getJugadores();

    assertEquals(2, jugadores.size());
    assertSame(jugadorUno, jugadores.get(0));
    assertSame(jugadorDos, jugadores.get(1));
  }

  @Test
  public void alAvanzarTurnoDebePasarAlSiguienteJugadorYReiniciarDatosDelTurno() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 20, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));
    partida.setJugadorEnTurno(jugadorUno);
    partida.setEtapaActual(2);

    partida.avanzarTurno();

    assertSame(jugadorDos, partida.getJugadorEnTurno());
    assertEquals(1, partida.getEtapaActual());
    assertEquals(1, partida.getRondaActual());
    assertNotNull(partida.getInicioEtapa());
    assertFalse(partida.alcanzoLimiteConquistas());
  }

  @Test
  public void alAvanzarTurnoDesdeUltimoJugadorDebeIncrementarRondaYVolverAlPrimero() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 20, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));
    partida.setJugadorEnTurno(jugadorDos);
    partida.setRondaActual(1);

    partida.avanzarTurno();

    assertSame(jugadorUno, partida.getJugadorEnTurno());
    assertEquals(2, partida.getRondaActual());
    assertEquals(1, partida.getEtapaActual());
    assertNotNull(partida.getInicioEtapa());
  }

  @Test
  public void alAvanzarTurnoSiLaPartidaFinalizaNoDebeCambiarElJugadorEnTurno() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 20, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));
    partida.setJugadorEnTurno(jugadorDos);
    partida.setRondaActual(6);

    partida.avanzarTurno();

    assertSame(jugadorDos, partida.getJugadorEnTurno());
    assertEquals(7, partida.getRondaActual());
    assertTrue(partida.estaFinalizada());
  }

  @Test
  public void alAvanzarTurnoSinJugadoresNoDebeModificarLaPartida() {
    Partida partida = new Partida();

    partida.setJugadores(List.of());
    partida.setRondaActual(1);

    partida.avanzarTurno();

    assertEquals(1, partida.getRondaActual());
    assertEquals(null, partida.getJugadorEnTurno());
  }

  @Test
  public void alVerificarTiempoAgotadoSinInicioDeEtapaDebeRetornarFalse() {
    Partida partida = new Partida();

    assertFalse(partida.tieneTiempoAgotado(10));
  }

  @Test
  public void alVerificarTiempoAgotadoConTiempoDisponibleDebeRetornarFalse() {
    Partida partida = new Partida();

    partida.setInicioEtapa(LocalDateTime.now().minusSeconds(2));

    assertFalse(partida.tieneTiempoAgotado(10));
  }

  @Test
  public void alVerificarTiempoAgotadoConTiempoVencidoDebeRetornarTrue() {
    Partida partida = new Partida();

    partida.setInicioEtapa(LocalDateTime.now().minusSeconds(20));

    assertTrue(partida.tieneTiempoAgotado(10));
  }

  @Test
  public void alConsultarSiEsTurnoDeJugadorDebeRetornarTrueSiCoincideElId() {
    Partida partida = new Partida();

    Jugador jugador = crearJugador(1L, "Santi", 10, null, "ROJO");

    partida.setJugadorEnTurno(jugador);

    assertTrue(partida.esTurnoDe(1L));
  }

  @Test
  public void alConsultarSiEsTurnoDeJugadorDebeRetornarFalseSiNoHayJugadorEnTurnoOId() {
    Partida partida = new Partida();

    assertFalse(partida.esTurnoDe(1L));
    assertFalse(partida.esTurnoDe(null));
  }

  @Test
  public void alConsultarRondaActualSiEsNullDebeRetornarUno() {
    Partida partida = new Partida();

    partida.setRondaActual(null);

    assertEquals(1, partida.getRondaActual());
  }

  @Test
  public void alConsultarSiEstaFinalizadaConRondaNullDebeInicializarEnCeroYRetornarFalse() {
    Partida partida = new Partida();

    partida.setRondaActual(null);

    assertFalse(partida.estaFinalizada());
    assertEquals(0, partida.getRondaActual());
  }

  @Test
  public void alSuperarLaCantidadMaximaDeRondasDebeEstarFinalizada() {
    Partida partida = new Partida();

    partida.setRondaActual(7);

    assertTrue(partida.estaFinalizada());
  }

  @Test
  public void alObtenerRankingDebeOrdenarJugadoresPorPuntajeDescendente() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 50, null, "AZUL");
    Jugador jugadorTres = crearJugador(3L, "Tomi", 30, null, "VERDE");

    partida.setJugadores(List.of(jugadorUno, jugadorDos, jugadorTres));

    List<Jugador> ranking = partida.obtenerRanking();

    assertSame(jugadorDos, ranking.get(0));
    assertSame(jugadorTres, ranking.get(1));
    assertSame(jugadorUno, ranking.get(2));
  }

  @Test
  public void alRegistrarTresConquistasDebeAlcanzarLimiteDeConquistas() {
    Partida partida = new Partida();

    partida.registrarConquista();
    partida.registrarConquista();

    assertFalse(partida.alcanzoLimiteConquistas());

    partida.registrarConquista();

    assertTrue(partida.alcanzoLimiteConquistas());
  }

  @Test
  public void alRegistrarYReiniciarPreguntasHechasDebeActualizarElSet() {
    Partida partida = new Partida();

    partida.registrarPreguntaHecha(10L);
    partida.registrarPreguntaHecha(20L);

    Set<Long> preguntasHechas = partida.getPreguntasHechas();

    assertEquals(2, preguntasHechas.size());
    assertTrue(preguntasHechas.contains(10L));
    assertTrue(preguntasHechas.contains(20L));

    partida.reiniciarPreguntasHechas();

    assertTrue(partida.getPreguntasHechas().isEmpty());
  }

  @Test
  public void alCalcularPuestoDeUsuarioDebeRetornarLaPosicionDelJugadorAsociado() {
    Partida partida = new Partida();

    Usuario usuarioBuscado = crearUsuario(10L);
    Usuario otroUsuario = crearUsuario(20L);

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, usuarioBuscado, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 50, otroUsuario, "AZUL");
    Jugador jugadorTres = crearJugador(3L, "Invitado", 30, null, "VERDE");

    partida.setJugadores(List.of(jugadorUno, jugadorDos, jugadorTres));

    int puesto = partida.calcularPuestoDeUsuario(10L);

    assertEquals(3, puesto);
  }

  @Test
  public void alCalcularPuestoDeUsuarioSiNoEncuentraUsuarioDebeRetornarTres() {
    Partida partida = new Partida();

    Usuario otroUsuario = crearUsuario(20L);

    Jugador jugadorUno = crearJugador(1L, "Santi", 50, otroUsuario, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Invitado", 30, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));

    int puesto = partida.calcularPuestoDeUsuario(99L);

    assertEquals(3, puesto);
  }

  @Test
  public void alObtenerNombreGanadorDebeRetornarElNombreDelPrimeroDelRanking() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 50, null, "AZUL");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));

    String nombreGanador = partida.obtenerNombreGanador();

    assertEquals("Mateo", nombreGanador);
  }

  @Test
  public void alObtenerMapaDeColoresDebeRetornarColoresEnMinusculaPorIdJugador() {
    Partida partida = new Partida();

    Jugador jugadorUno = crearJugador(1L, "Santi", 10, null, "ROJO");
    Jugador jugadorDos = crearJugador(2L, "Mateo", 20, null, "AzUl");

    partida.setJugadores(List.of(jugadorUno, jugadorDos));

    Map<Long, String> mapa = partida.obtenerMapaDeColoresPorJugador();

    assertEquals(2, mapa.size());
    assertEquals("rojo", mapa.get(1L));
    assertEquals("azul", mapa.get(2L));
  }

  @Test
  public void alObtenerMapaDeColoresDebeIgnorarJugadoresInvalidos() {
    Partida partida = new Partida();

    Jugador jugadorSinId = crearJugador(null, "Santi", 10, null, "ROJO");
    Jugador jugadorSinColor = crearJugador(2L, "Mateo", 20, null, null);
    Jugador jugadorValido = crearJugador(3L, "Tomi", 30, null, "VERDE");

    partida.setJugadores(Arrays.asList(null, jugadorSinId, jugadorSinColor, jugadorValido));

    Map<Long, String> mapa = partida.obtenerMapaDeColoresPorJugador();

    assertEquals(1, mapa.size());
    assertEquals("verde", mapa.get(3L));
  }

  @Test
  public void losSettersYGettersBasicosDebenGuardarYRetornarValores() {
    Partida partida = new Partida();

    Jugador jugador = crearJugador(1L, "Santi", 10, null, "ROJO");
    LocalDateTime inicio = LocalDateTime.now();

    partida.setJugadorEnTurno(jugador);
    partida.setEtapaActual(2);
    partida.setInicioEtapa(inicio);
    partida.setRondaActual(4);

    assertSame(jugador, partida.getJugadorEnTurno());
    assertEquals(2, partida.getEtapaActual());
    assertSame(inicio, partida.getInicioEtapa());
    assertEquals(4, partida.getRondaActual());
  }

  private Jugador crearJugador(
    Long id,
    String nombre,
    Integer puntaje,
    Usuario usuario,
    String color
  ) {
    Jugador jugador = mock(Jugador.class);

    when(jugador.getId()).thenReturn(id);
    when(jugador.getNombre()).thenReturn(nombre);
    when(jugador.getPuntaje()).thenReturn(puntaje);
    when(jugador.getUsuario()).thenReturn(usuario);
    when(jugador.getColor()).thenReturn(color);

    return jugador;
  }

  private Usuario crearUsuario(Long id) {
    Usuario usuario = mock(Usuario.class);

    when(usuario.getId()).thenReturn(id);

    return usuario;
  }
}
