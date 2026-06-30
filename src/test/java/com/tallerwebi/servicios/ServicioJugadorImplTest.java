package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioJugador;
import com.tallerwebi.servicios.Impl.ServicioJugadorImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioJugadorImplTest {

  /*
   * Mockito:
   *
   * Mockeamos el repositorio porque este test es unitario.
   * No queremos ir a la base de datos.
   */
  @Mock
  private RepositorioJugador repositorioJugador;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioJugadorImpl
   * e inyecta el mock repositorioJugador en el constructor.
   */
  @InjectMocks
  private ServicioJugadorImpl servicioJugador;

  @Test
  public void alCrearJugadorDebeRetornarJugadorConNombreColorYSinUsuario() {
    /*
     * Este test cubre crearJugador().
     *
     * El método debe crear un jugador común,
     * sin usuario asociado.
     */

    // Preparación
    String nombre = "Jugador Dos";
    String color = "Azul";

    // Ejecución
    Jugador jugador = servicioJugador.crearJugador(nombre, color);

    // Verificación
    assertEquals(nombre, jugador.getNombre());
    assertEquals(color, jugador.getColor());
    assertNull(jugador.getUsuario());
  }

  @Test
  public void alCrearJugadorConUsuarioDebeRetornarJugadorConNombreDelUsuarioColorYUsuario() {
    /*
     * Este test cubre crearJugadorConUsuario().
     *
     * El método usa:
     *
     * usuario.getNombreJugador()
     *
     * para crear el jugador anfitrión.
     */

    // Preparación
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(usuario.getNombreJugador()).thenReturn("Santi");

    String color = "Rojo";

    // Ejecución
    Jugador jugador = servicioJugador.crearJugadorConUsuario(usuario, color);

    // Verificación
    assertEquals("Santi", jugador.getNombre());
    assertEquals(color, jugador.getColor());
    assertSame(usuario, jugador.getUsuario());
  }

  @Test
  public void alGuardarJugadorDebeLlamarAlRepositorio() {
    /*
     * Este test cubre guardar().
     */

    // Preparación
    Jugador jugador = org.mockito.Mockito.mock(Jugador.class);

    // Ejecución
    servicioJugador.guardar(jugador);

    // Verificación
    verify(repositorioJugador).guardar(jugador);
  }

  @Test
  public void alActualizarJugadorDebeLlamarAlRepositorio() {
    /*
     * Este test cubre actualizar().
     */

    // Preparación
    Jugador jugador = org.mockito.Mockito.mock(Jugador.class);

    // Ejecución
    servicioJugador.actualizar(jugador);

    // Verificación
    verify(repositorioJugador).actualizar(jugador);
  }

  @Test
  public void alObtenerTodosDebeRetornarLaListaDelRepositorio() {
    /*
     * Este test cubre obtenerTodos().
     *
     * El servicio solamente delega en repositorioJugador.buscarTodos().
     */

    // Preparación
    Jugador jugadorUno = org.mockito.Mockito.mock(Jugador.class);
    Jugador jugadorDos = org.mockito.Mockito.mock(Jugador.class);

    List<Jugador> jugadoresEsperados = List.of(jugadorUno, jugadorDos);

    when(repositorioJugador.buscarTodos()).thenReturn(jugadoresEsperados);

    // Ejecución
    List<Jugador> jugadoresObtenidos = servicioJugador.obtenerTodos();

    // Verificación
    assertSame(jugadoresEsperados, jugadoresObtenidos);
    verify(repositorioJugador).buscarTodos();
  }

  @Test
  public void alBuscarPorIdDebeRetornarElJugadorDelRepositorio() {
    /*
     * Este test cubre buscarPorId().
     */

    // Preparación
    Long jugadorId = 1L;
    Jugador jugadorEsperado = org.mockito.Mockito.mock(Jugador.class);

    when(repositorioJugador.buscarPorId(jugadorId)).thenReturn(jugadorEsperado);

    // Ejecución
    Jugador jugadorObtenido = servicioJugador.buscarPorId(jugadorId);

    // Verificación
    assertSame(jugadorEsperado, jugadorObtenido);
    verify(repositorioJugador).buscarPorId(jugadorId);
  }
}
