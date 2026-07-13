package com.tallerwebi.controladores.clasesAuxiliares;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class DatosSugerenciaPreguntaTest {

  @Test
  public void alCrearDatosSugerenciaPreguntaConConstructorVacioDebeInicializarCamposEnNull() {
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    assertNull(datos.getId());
    assertNull(datos.getEnunciado());
    assertNull(datos.getRespuestaCorrecta());
    assertNull(datos.getOpcionIncorrectaUno());
    assertNull(datos.getOpcionIncorrectaDos());
    assertNull(datos.getOpcionIncorrectaTres());
    assertNull(datos.getIdProvincia());
  }

  @Test
  public void losSettersDebenGuardarYLosGettersDebenRetornarLosValoresAsignados() {
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    datos.setId(10L);
    datos.setEnunciado("¿Cuál es la capital de Buenos Aires?");
    datos.setRespuestaCorrecta("La Plata");
    datos.setOpcionIncorrectaUno("Mar del Plata");
    datos.setOpcionIncorrectaDos("Bahía Blanca");
    datos.setOpcionIncorrectaTres("Tandil");
    datos.setIdProvincia(1L);

    assertEquals(10L, datos.getId());
    assertEquals("¿Cuál es la capital de Buenos Aires?", datos.getEnunciado());
    assertEquals("La Plata", datos.getRespuestaCorrecta());
    assertEquals("Mar del Plata", datos.getOpcionIncorrectaUno());
    assertEquals("Bahía Blanca", datos.getOpcionIncorrectaDos());
    assertEquals("Tandil", datos.getOpcionIncorrectaTres());
    assertEquals(1L, datos.getIdProvincia());
  }

  @Test
  public void losSettersDebenPermitirAsignarNull() {
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    datos.setId(10L);
    datos.setEnunciado("Pregunta");
    datos.setRespuestaCorrecta("Correcta");
    datos.setOpcionIncorrectaUno("Incorrecta 1");
    datos.setOpcionIncorrectaDos("Incorrecta 2");
    datos.setOpcionIncorrectaTres("Incorrecta 3");
    datos.setIdProvincia(1L);

    datos.setId(null);
    datos.setEnunciado(null);
    datos.setRespuestaCorrecta(null);
    datos.setOpcionIncorrectaUno(null);
    datos.setOpcionIncorrectaDos(null);
    datos.setOpcionIncorrectaTres(null);
    datos.setIdProvincia(null);

    assertNull(datos.getId());
    assertNull(datos.getEnunciado());
    assertNull(datos.getRespuestaCorrecta());
    assertNull(datos.getOpcionIncorrectaUno());
    assertNull(datos.getOpcionIncorrectaDos());
    assertNull(datos.getOpcionIncorrectaTres());
    assertNull(datos.getIdProvincia());
  }
}
