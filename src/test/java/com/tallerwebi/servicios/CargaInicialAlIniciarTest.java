package com.tallerwebi.servicios;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.event.ContextRefreshedEvent;

@ExtendWith(MockitoExtension.class)
public class CargaInicialAlIniciarTest {

  /*
   * Mockito:
   *
   * Mockeamos ServicioCargaInicial porque no queremos ejecutar la carga real
   * de provincias, preguntas, roles o usuario admin.
   *
   * Solo queremos verificar que CargaInicialAlIniciar lo llame correctamente.
   */
  @Mock
  private ServicioCargaInicial servicioCargaInicial;

  /*
   * Mockito:
   *
   * Mockeamos el evento de Spring porque el método lo recibe como parámetro,
   * pero dentro de la clase no se usa su contenido.
   */
  @Mock
  private ContextRefreshedEvent contextRefreshedEvent;

  @Test
  public void alIniciarLaAplicacionDebeCargarLosDatosInicialesUnaSolaVez() {
    /*
     * Este test cubre las dos ramas del if:
     *
     * Primera ejecución:
     * cargaRealizada == false
     * entonces llama a cargarDatosIniciales()
     *
     * Segunda ejecución:
     * cargaRealizada == true
     * entonces NO vuelve a llamar a cargarDatosIniciales()
     */

    // Preparación
    CargaInicialAlIniciar cargaInicialAlIniciar = new CargaInicialAlIniciar(servicioCargaInicial);

    // Ejecución
    cargaInicialAlIniciar.onApplicationEvent(contextRefreshedEvent);
    cargaInicialAlIniciar.onApplicationEvent(contextRefreshedEvent);

    // Verificación
    verify(servicioCargaInicial, times(1)).cargarDatosIniciales();
  }
}
