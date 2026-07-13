package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class EstadoSugerenciaPreguntaTest {

  @Test
  public void debeTenerEstadoPendiente() {
    EstadoSugerenciaPregunta estado = EstadoSugerenciaPregunta.PENDIENTE;

    assertNotNull(estado);
    assertEquals("PENDIENTE", estado.name());
  }

  @Test
  public void debeTenerEstadoAprobada() {
    EstadoSugerenciaPregunta estado = EstadoSugerenciaPregunta.APROBADA;

    assertNotNull(estado);
    assertEquals("APROBADA", estado.name());
  }

  @Test
  public void debeTenerEstadoRechazada() {
    EstadoSugerenciaPregunta estado = EstadoSugerenciaPregunta.RECHAZADA;

    assertNotNull(estado);
    assertEquals("RECHAZADA", estado.name());
  }

  @Test
  public void debeTenerTresEstadosDisponibles() {
    EstadoSugerenciaPregunta[] estados = EstadoSugerenciaPregunta.values();

    assertEquals(3, estados.length);
    assertEquals(EstadoSugerenciaPregunta.PENDIENTE, estados[0]);
    assertEquals(EstadoSugerenciaPregunta.APROBADA, estados[1]);
    assertEquals(EstadoSugerenciaPregunta.RECHAZADA, estados[2]);
  }

  @Test
  public void valueOfDebeConvertirTextoAEstado() {
    EstadoSugerenciaPregunta pendiente = EstadoSugerenciaPregunta.valueOf("PENDIENTE");
    EstadoSugerenciaPregunta aprobada = EstadoSugerenciaPregunta.valueOf("APROBADA");
    EstadoSugerenciaPregunta rechazada = EstadoSugerenciaPregunta.valueOf("RECHAZADA");

    assertEquals(EstadoSugerenciaPregunta.PENDIENTE, pendiente);
    assertEquals(EstadoSugerenciaPregunta.APROBADA, aprobada);
    assertEquals(EstadoSugerenciaPregunta.RECHAZADA, rechazada);
  }
}
