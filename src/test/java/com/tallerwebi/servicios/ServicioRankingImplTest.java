package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.Impl.ServicioRankingImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioRankingImplTest {

  /*
   * Mockito:
   *
   * Mockeamos RepositorioUsuario porque ServicioRankingImpl
   * solo consulta datos de ranking.
   *
   * No queremos consultar una base real.
   */
  @Mock
  private RepositorioUsuario repositorioUsuario;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioRankingImpl
   * e inyecta el mock repositorioUsuario en el constructor.
   */
  @InjectMocks
  private ServicioRankingImpl servicioRanking;

  @Test
  public void alObtenerTop10GeneralDebeRetornarElTop10DelRepositorio() {
    /*
     * Cubre obtenerTop10General().
     *
     * El servicio simplemente delega en:
     * repositorioUsuario.obtenerTop10Historico()
     */

    // Preparación
    Usuario usuarioUno = org.mockito.Mockito.mock(Usuario.class);
    Usuario usuarioDos = org.mockito.Mockito.mock(Usuario.class);

    List<Usuario> top10Esperado = List.of(usuarioUno, usuarioDos);

    when(repositorioUsuario.obtenerTop10Historico()).thenReturn(top10Esperado);

    // Ejecución
    List<Usuario> top10Obtenido = servicioRanking.obtenerTop10General();

    // Verificación
    assertSame(top10Esperado, top10Obtenido);
    verify(repositorioUsuario).obtenerTop10Historico();
  }

  @Test
  public void alCalcularPuestoConUsuarioNullDebeRetornarCero() {
    /*
     * Cubre esta rama:
     *
     * if (usuario == null) {
     *   return 0L;
     * }
     *
     * Además verifica que no consulte el repositorio.
     */

    // Ejecución
    Long puesto = servicioRanking.calcularPuestoUsuario(null);

    // Verificación
    assertEquals(0L, puesto);

    verify(repositorioUsuario, never()).obtenerPosicionEnRanking(any(), anyString());
  }

  @Test
  public void alCalcularPuestoConUsuarioValidoDebeRetornarCantidadMejoresMasUno() {
    /*
     * Cubre el caso normal:
     *
     * - usuario existe
     * - tiene experiencia
     * - tiene nombre
     * - el repositorio devuelve cantidad de usuarios mejores
     *
     * Si hay 4 mejores, el puesto del usuario es 5.
     */

    // Preparación
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(usuario.getExperiencia()).thenReturn(100);
    when(usuario.getNombreJugador()).thenReturn("Santi");

    when(repositorioUsuario.obtenerPosicionEnRanking(100, "Santi")).thenReturn(4L);

    // Ejecución
    Long puesto = servicioRanking.calcularPuestoUsuario(usuario);

    // Verificación
    assertEquals(5L, puesto);

    verify(repositorioUsuario).obtenerPosicionEnRanking(100, "Santi");
  }

  @Test
  public void alCalcularPuestoConNombreNullDebeUsarStringVacio() {
    /*
     * Cubre esta rama:
     *
     * usuario.getNombreJugador() != null ? nombre : ""
     *
     * Si el nombre es null, el servicio debe buscar con "".
     */

    // Preparación
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(usuario.getExperiencia()).thenReturn(50);
    when(usuario.getNombreJugador()).thenReturn(null);

    when(repositorioUsuario.obtenerPosicionEnRanking(50, "")).thenReturn(2L);

    // Ejecución
    Long puesto = servicioRanking.calcularPuestoUsuario(usuario);

    // Verificación
    assertEquals(3L, puesto);

    verify(repositorioUsuario).obtenerPosicionEnRanking(50, "");
  }

  @Test
  public void alCalcularPuestoCuandoRepositorioDevuelveNullDebeTomarloComoCero() {
    /*
     * Cubre esta rama:
     *
     * cantidadMejores != null ? cantidadMejores : 0L
     *
     * Si el repositorio devuelve null,
     * el usuario queda en puesto 1.
     */

    // Preparación
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(usuario.getExperiencia()).thenReturn(200);
    when(usuario.getNombreJugador()).thenReturn("Axel");

    when(repositorioUsuario.obtenerPosicionEnRanking(200, "Axel")).thenReturn(null);

    // Ejecución
    Long puesto = servicioRanking.calcularPuestoUsuario(usuario);

    // Verificación
    assertEquals(1L, puesto);

    verify(repositorioUsuario).obtenerPosicionEnRanking(200, "Axel");
  }
}
