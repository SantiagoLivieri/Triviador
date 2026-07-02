package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class PaqueteMonedasTest {

  @Test
  public void alCrearPaqueteVacioLosAtributosDebenSerNulos() {
    PaqueteMonedas paquete = new PaqueteMonedas();

    assertNull(paquete.getId());
    assertNull(paquete.getTitulo());
    assertNull(paquete.getCantidadCoins());
    assertNull(paquete.getPrecioArs());
    assertNull(paquete.getBeneficioExtra());
  }

  @Test
  public void alCrearPaqueteConParametrosDebeAsignarLosAtributosCorrectamente() {
    String titulo = "Paquete Básico";
    Integer cantidadCoins = 500;
    Double precioArs = 1500.0;
    String beneficioExtra = "10% extra";

    PaqueteMonedas paquete = new PaqueteMonedas(titulo, cantidadCoins, precioArs, beneficioExtra);

    assertNull(paquete.getId());
    assertEquals(titulo, paquete.getTitulo());
    assertEquals(cantidadCoins, paquete.getCantidadCoins());
    assertEquals(precioArs, paquete.getPrecioArs());
    assertEquals(beneficioExtra, paquete.getBeneficioExtra());
  }

  @Test
  public void alUsarSettersDebeActualizarLosAtributosCorrectamente() {
    PaqueteMonedas paquete = new PaqueteMonedas();

    Long id = 1L;
    String titulo = "Paquete Premium";
    Integer cantidadCoins = 1000;
    Double precioArs = 2800.0;
    String beneficioExtra = "20% extra";

    paquete.setId(id);
    paquete.setTitulo(titulo);
    paquete.setCantidadCoins(cantidadCoins);
    paquete.setPrecioArs(precioArs);
    paquete.setBeneficioExtra(beneficioExtra);

    assertEquals(id, paquete.getId());
    assertEquals(titulo, paquete.getTitulo());
    assertEquals(cantidadCoins, paquete.getCantidadCoins());
    assertEquals(precioArs, paquete.getPrecioArs());
    assertEquals(beneficioExtra, paquete.getBeneficioExtra());
  }
}
