package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.Impl.ServicioCargaPreguntasImpl;
import com.tallerwebi.servicios.excepcion.CargaPreguntasException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioCargaPreguntasImplTest {

  /*
   * Mockito:
   *
   * Mockeamos RepositorioPregunta porque este test es unitario.
   * No queremos contar preguntas reales desde la base de datos.
   */
  @Mock
  private RepositorioPregunta repositorioPregunta;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioCargaPreguntasImpl
   * e inyecta el mock repositorioPregunta en el constructor.
   */
  @InjectMocks
  private ServicioCargaPreguntasImpl servicioCargaPreguntas;

  @Test
  public void alEjecutarCargaInicialConCantidadSuficienteNoDebeLanzarExcepcion() {
    /*
     * Cubre el caso correcto:
     *
     * cantidadActual >= CANTIDAD_MINIMA_PREGUNTAS
     *
     * Como el mínimo es 15, si el repositorio devuelve 15,
     * la carga inicial se considera válida.
     */

    // Preparación
    when(repositorioPregunta.contar()).thenReturn(15L);

    // Ejecución + Verificación
    assertDoesNotThrow(() -> servicioCargaPreguntas.ejecutarCargaInicial());

    verify(repositorioPregunta).contar();
  }

  @Test
  public void alEjecutarCargaInicialConCantidadInsuficienteDebeLanzarCargaPreguntasException() {
    /*
     * Cubre esta rama:
     *
     * if (cantidadActual < CANTIDAD_MINIMA_PREGUNTAS)
     *
     * Si hay menos de 15 preguntas, debe lanzar CargaPreguntasException.
     */

    // Preparación
    when(repositorioPregunta.contar()).thenReturn(14L);

    // Ejecución + Verificación
    assertThrows(
      CargaPreguntasException.class,
      () -> servicioCargaPreguntas.ejecutarCargaInicial()
    );

    verify(repositorioPregunta).contar();
  }
}
