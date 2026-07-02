package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class PagoProcesadoTest {

  @Test
  public void alCrearPagoProcesadoVacioElIdDebeSerNulo() {
    PagoProcesado pago = new PagoProcesado();

    assertNull(pago.getIdMercadoPago());
  }

  @Test
  public void alCrearPagoProcesadoConParametroDebeAsignarElIdCorrectamente() {
    Long idEsperado = 123456789L;

    PagoProcesado pago = new PagoProcesado(idEsperado);

    assertEquals(idEsperado, pago.getIdMercadoPago());
  }

  @Test
  public void alUsarSetterDebeActualizarElIdCorrectamente() {
    PagoProcesado pago = new PagoProcesado();
    Long idEsperado = 987654321L;

    pago.setIdMercadoPago(idEsperado);

    assertEquals(idEsperado, pago.getIdMercadoPago());
  }
}
