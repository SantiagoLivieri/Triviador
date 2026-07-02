package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.servicios.Impl.ServicioPartidaImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioPartidaImplTest {

  /*
   * Mockito:
   *
   * Mockeamos RepositorioPartida porque este test es unitario.
   * No queremos guardar una partida real en la base de datos.
   */
  @Mock
  private RepositorioPartida repositorioPartida;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioPartidaImpl
   * e inyecta el repositorio mockeado en el constructor.
   */
  @InjectMocks
  private ServicioPartidaImpl servicioPartida;

  @Test
  public void alCrearPartidaDebeGuardarLaPartidaYRetornarlaConfigurada() {
    /*
     * Este test cubre crearPartida().
     *
     * El método debe:
     * 1. crear una Partida nueva
     * 2. mezclar la lista de jugadores
     * 3. asignar los jugadores a la partida
     * 4. asignar el jugador en turno
     * 5. setear etapaActual en 1
     * 6. setear inicioEtapa
     * 7. guardar la partida
     * 8. retornar esa misma partida
     *
     * Importante:
     * Como el método usa Collections.shuffle(),
     * NO podemos validar un orden exacto de jugadores.
     * Solo validamos que estén los mismos jugadores.
     */

    // Preparación
    Jugador jugadorUno = org.mockito.Mockito.mock(Jugador.class);
    Jugador jugadorDos = org.mockito.Mockito.mock(Jugador.class);
    Jugador jugadorTres = org.mockito.Mockito.mock(Jugador.class);

    when(jugadorUno.getId()).thenReturn(1L);
    when(jugadorDos.getId()).thenReturn(2L);
    when(jugadorTres.getId()).thenReturn(3L);

    List<Jugador> jugadores = List.of(jugadorUno, jugadorDos, jugadorTres);

    // Ejecución
    Partida partidaRetornada = servicioPartida.crearPartida(jugadores);

    /*
     * Mockito:
     *
     * ArgumentCaptor nos permite capturar la Partida real
     * que ServicioPartidaImpl le mandó al repositorio.
     */
    ArgumentCaptor<Partida> captorPartida = ArgumentCaptor.forClass(Partida.class);

    // Verificación
    verify(repositorioPartida).guardar(captorPartida.capture());

    Partida partidaGuardada = captorPartida.getValue();

    assertSame(partidaGuardada, partidaRetornada);

    assertNotNull(partidaGuardada.getJugadores());
    assertEquals(3, partidaGuardada.getJugadores().size());
    assertTrue(partidaGuardada.getJugadores().containsAll(jugadores));

    assertNotNull(partidaGuardada.getJugadorEnTurno());
    assertTrue(jugadores.contains(partidaGuardada.getJugadorEnTurno()));

    assertEquals(1, partidaGuardada.getEtapaActual());
    assertNotNull(partidaGuardada.getInicioEtapa());
  }

  @Test
  public void alBuscarPorIdDebeRetornarLaPartidaDelRepositorio() {
    /*
     * Este test cubre buscarPorId().
     *
     * El servicio solamente delega la búsqueda al repositorio.
     */

    // Preparación
    Long partidaId = 1L;
    Partida partidaEsperada = new Partida();

    when(repositorioPartida.buscarPorId(partidaId)).thenReturn(partidaEsperada);

    // Ejecución
    Partida partidaObtenida = servicioPartida.buscarPorId(partidaId);

    // Verificación
    assertSame(partidaEsperada, partidaObtenida);
    verify(repositorioPartida).buscarPorId(partidaId);
  }

  @Test
  public void alActualizarDebeLlamarAlRepositorio() {
    /*
     * Este test cubre actualizar().
     *
     * El servicio recibe una partida y debe mandarla al repositorio.
     */

    // Preparación
    Partida partida = new Partida();

    // Ejecución
    servicioPartida.actualizar(partida);

    // Verificación
    verify(repositorioPartida).actualizar(partida);
  }
}
