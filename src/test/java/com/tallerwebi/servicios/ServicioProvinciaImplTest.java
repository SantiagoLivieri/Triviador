package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.servicios.Impl.ServicioProvinciaImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioProvinciaImplTest {

  /*
   * Mockito:
   *
   * Mockeamos RepositorioProvincia porque este test es unitario.
   * No queremos consultar ni actualizar la base real.
   */
  @Mock
  private RepositorioProvincia repositorioProvincia;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioProvinciaImpl
   * e inyecta el repositorio mockeado en el constructor.
   */
  @InjectMocks
  private ServicioProvinciaImpl servicioProvincia;

  @Test
  public void alBuscarPorIdDebeRetornarLaProvinciaDelRepositorio() {
    /*
     * Cubre buscarPorId().
     *
     * El servicio simplemente delega en repositorioProvincia.buscarPorId().
     */

    // Preparación
    Long provinciaId = 1L;
    Provincia provinciaEsperada = org.mockito.Mockito.mock(Provincia.class);

    when(repositorioProvincia.buscarPorId(provinciaId)).thenReturn(provinciaEsperada);

    // Ejecución
    Provincia provinciaObtenida = servicioProvincia.buscarPorId(provinciaId);

    // Verificación
    assertSame(provinciaEsperada, provinciaObtenida);
    verify(repositorioProvincia).buscarPorId(provinciaId);
  }

  @Test
  public void alObtenerProvinciasDebeRetornarTodasLasProvinciasDelRepositorio() {
    /*
     * Cubre obtenerProvincias().
     */

    // Preparación
    Provincia provinciaUno = org.mockito.Mockito.mock(Provincia.class);
    Provincia provinciaDos = org.mockito.Mockito.mock(Provincia.class);

    List<Provincia> provinciasEsperadas = List.of(provinciaUno, provinciaDos);

    when(repositorioProvincia.buscarTodas()).thenReturn(provinciasEsperadas);

    // Ejecución
    List<Provincia> provinciasObtenidas = servicioProvincia.obtenerProvincias();

    // Verificación
    assertSame(provinciasEsperadas, provinciasObtenidas);
    verify(repositorioProvincia).buscarTodas();
  }

  @Test
  public void alResetearProvinciasDebeLlamarAlRepositorio() {
    /*
     * Cubre resetearProvincias().
     */

    // Ejecución
    servicioProvincia.resetearProvincias();

    // Verificación
    verify(repositorioProvincia).resetearProvincias();
  }

  @Test
  public void alActualizarProvinciaValidaDebeLlamarAlRepositorio() {
    /*
     * Cubre actualizar() con provincia válida.
     *
     * Para que sea válida:
     * - provincia no puede ser null
     * - provincia.getId() no puede ser null
     */

    // Preparación
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(provincia.getId()).thenReturn(1L);

    // Ejecución
    servicioProvincia.actualizar(provincia);

    // Verificación
    verify(repositorioProvincia).actualizar(provincia);
  }

  @Test
  public void alActualizarProvinciaNullDebeLanzarIllegalArgumentException() {
    /*
     * Cubre esta parte:
     *
     * provincia == null
     */

    // Ejecución + Verificación
    assertThrows(IllegalArgumentException.class, () -> servicioProvincia.actualizar(null));

    verify(repositorioProvincia, never()).actualizar(any(Provincia.class));
  }

  @Test
  public void alActualizarProvinciaSinIdDebeLanzarIllegalArgumentException() {
    /*
     * Cubre esta parte:
     *
     * provincia.getId() == null
     */

    // Preparación
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(provincia.getId()).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(IllegalArgumentException.class, () -> servicioProvincia.actualizar(provincia));

    verify(repositorioProvincia, never()).actualizar(any(Provincia.class));
  }

  @Test
  public void alObtenerCantidadPreguntasDeProvinciaNeutralDebeRetornarUno() {
    /*
     * Cubre obtenerCantidadPreguntasRequeridas()
     * cuando la provincia existe y es neutral.
     *
     * Una provincia neutral requiere 1 pregunta.
     */

    // Preparación
    Long provinciaId = 1L;
    Provincia provincia = new Provincia("Buenos Aires", 0);

    when(repositorioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);

    // Ejecución
    Integer cantidad = servicioProvincia.obtenerCantidadPreguntasRequeridas(provinciaId);

    // Verificación
    assertEquals(1, cantidad);
    verify(repositorioProvincia).buscarPorId(provinciaId);
  }

  @Test
  public void alObtenerCantidadPreguntasDeProvinciaConDuenioDebeRetornarTres() {
    /*
     * Cubre obtenerCantidadPreguntasRequeridas()
     * cuando la provincia existe y ya tiene dueño.
     *
     * Una provincia con dueño requiere 3 preguntas.
     */

    // Preparación
    Long provinciaId = 1L;
    Provincia provincia = new Provincia("Cordoba", 0);
    provincia.setIdJugadorDuenio(10L);

    when(repositorioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);

    // Ejecución
    Integer cantidad = servicioProvincia.obtenerCantidadPreguntasRequeridas(provinciaId);

    // Verificación
    assertEquals(3, cantidad);
    verify(repositorioProvincia).buscarPorId(provinciaId);
  }

  @Test
  public void alObtenerCantidadPreguntasDeProvinciaInexistenteDebeLanzarIllegalArgumentException() {
    /*
     * Cubre esta rama:
     *
     * if (provincia == null)
     */

    // Preparación
    Long provinciaId = 99L;

    when(repositorioProvincia.buscarPorId(provinciaId)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioProvincia.obtenerCantidadPreguntasRequeridas(provinciaId)
    );

    verify(repositorioProvincia).buscarPorId(provinciaId);
  }
}
