package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class SugerenciaPreguntaTest {

  @Test
  public void alCrearSugerenciaDebeGuardarLosDatosInicialesConEstadoPendienteYFechaCreacion() {
    Provincia provincia = new Provincia("Buenos Aires", 0);
    Usuario usuarioCreador = new Usuario();

    SugerenciaPregunta sugerencia = new SugerenciaPregunta(
      "¿Cuál es la capital de Buenos Aires?",
      "La Plata",
      "Mar del Plata",
      "Bahía Blanca",
      "Tandil",
      provincia,
      usuarioCreador
    );

    assertEquals("¿Cuál es la capital de Buenos Aires?", sugerencia.getEnunciado());
    assertEquals("La Plata", sugerencia.getRespuestaCorrecta());
    assertEquals("Mar del Plata", sugerencia.getOpcionIncorrectaUno());
    assertEquals("Bahía Blanca", sugerencia.getOpcionIncorrectaDos());
    assertEquals("Tandil", sugerencia.getOpcionIncorrectaTres());

    assertSame(provincia, sugerencia.getProvincia());
    assertSame(usuarioCreador, sugerencia.getUsuarioCreador());

    assertEquals(EstadoSugerenciaPregunta.PENDIENTE, sugerencia.getEstado());

    assertNotNull(sugerencia.getFechaCreacion());
  }

  @Test
  public void alCrearSugerenciaConConstructorProtegidoDebeInicializarEstadoPendienteYFechaCreacion() {
    SugerenciaPregunta sugerencia = new SugerenciaPregunta();

    assertEquals(EstadoSugerenciaPregunta.PENDIENTE, sugerencia.getEstado());
    assertNotNull(sugerencia.getFechaCreacion());
  }

  @Test
  public void alAprobarSugerenciaDebeCambiarEstadoAAprobada() {
    SugerenciaPregunta sugerencia = crearSugerencia();

    sugerencia.aprobar();

    assertEquals(EstadoSugerenciaPregunta.APROBADA, sugerencia.getEstado());
  }

  @Test
  public void alRechazarSugerenciaDebeCambiarEstadoARechazada() {
    SugerenciaPregunta sugerencia = crearSugerencia();

    sugerencia.rechazar();

    assertEquals(EstadoSugerenciaPregunta.RECHAZADA, sugerencia.getEstado());
  }

  @Test
  public void alActualizarDatosDebeModificarCamposEditables() {
    SugerenciaPregunta sugerencia = crearSugerencia();

    Provincia nuevaProvincia = new Provincia("Córdoba", 0);

    sugerencia.actualizarDatos(
      "Nuevo enunciado",
      "Nueva correcta",
      "Nueva incorrecta 1",
      "Nueva incorrecta 2",
      "Nueva incorrecta 3",
      nuevaProvincia
    );

    assertEquals("Nuevo enunciado", sugerencia.getEnunciado());
    assertEquals("Nueva correcta", sugerencia.getRespuestaCorrecta());
    assertEquals("Nueva incorrecta 1", sugerencia.getOpcionIncorrectaUno());
    assertEquals("Nueva incorrecta 2", sugerencia.getOpcionIncorrectaDos());
    assertEquals("Nueva incorrecta 3", sugerencia.getOpcionIncorrectaTres());
    assertSame(nuevaProvincia, sugerencia.getProvincia());
  }

  @Test
  public void alActualizarDatosNoDebeCambiarUsuarioCreadorEstadoNiFechaCreacion() {
    SugerenciaPregunta sugerencia = crearSugerencia();

    Usuario usuarioOriginal = sugerencia.getUsuarioCreador();
    EstadoSugerenciaPregunta estadoOriginal = sugerencia.getEstado();
    LocalDateTime fechaOriginal = sugerencia.getFechaCreacion();

    Provincia nuevaProvincia = new Provincia("Mendoza", 0);

    sugerencia.actualizarDatos(
      "Pregunta editada",
      "Correcta editada",
      "Incorrecta editada 1",
      "Incorrecta editada 2",
      "Incorrecta editada 3",
      nuevaProvincia
    );

    assertSame(usuarioOriginal, sugerencia.getUsuarioCreador());
    assertEquals(estadoOriginal, sugerencia.getEstado());
    assertEquals(fechaOriginal, sugerencia.getFechaCreacion());
  }

  @Test
  public void losSettersDebenModificarLosCamposCorrespondientes() {
    SugerenciaPregunta sugerencia = new SugerenciaPregunta();

    Provincia provincia = new Provincia("Santa Fe", 0);
    Usuario usuario = new Usuario();

    sugerencia.setEnunciado("Enunciado seteado");
    sugerencia.setRespuestaCorrecta("Correcta seteada");
    sugerencia.setOpcionIncorrectaUno("Incorrecta 1 seteada");
    sugerencia.setOpcionIncorrectaDos("Incorrecta 2 seteada");
    sugerencia.setOpcionIncorrectaTres("Incorrecta 3 seteada");
    sugerencia.setProvincia(provincia);
    sugerencia.setUsuarioCreador(usuario);
    sugerencia.setEstado(EstadoSugerenciaPregunta.APROBADA);

    assertEquals("Enunciado seteado", sugerencia.getEnunciado());
    assertEquals("Correcta seteada", sugerencia.getRespuestaCorrecta());
    assertEquals("Incorrecta 1 seteada", sugerencia.getOpcionIncorrectaUno());
    assertEquals("Incorrecta 2 seteada", sugerencia.getOpcionIncorrectaDos());
    assertEquals("Incorrecta 3 seteada", sugerencia.getOpcionIncorrectaTres());
    assertSame(provincia, sugerencia.getProvincia());
    assertSame(usuario, sugerencia.getUsuarioCreador());
    assertEquals(EstadoSugerenciaPregunta.APROBADA, sugerencia.getEstado());
  }

  private SugerenciaPregunta crearSugerencia() {
    Provincia provincia = new Provincia("Buenos Aires", 0);
    Usuario usuarioCreador = new Usuario();

    return new SugerenciaPregunta(
      "Enunciado original",
      "Respuesta correcta original",
      "Incorrecta original 1",
      "Incorrecta original 2",
      "Incorrecta original 3",
      provincia,
      usuarioCreador
    );
  }
}
