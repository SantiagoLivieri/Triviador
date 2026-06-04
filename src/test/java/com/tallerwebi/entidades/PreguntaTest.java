package com.tallerwebi.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class PreguntaTest {

  @Test
  public void queSePuedaCrearUnaPreguntaConElConstructorVacio() {
    Pregunta pregunta = new Pregunta();

    assertThat(pregunta, is(notNullValue()));
    assertThat(pregunta.getId(), is(nullValue()));
    assertThat(pregunta.getEnunciado(), is(nullValue()));
  }

  @Test
  public void queSePuedaCrearUnaPreguntaCompletaConElConstructorParametrizado() {
    String enunciado = "En que año se fundo la UNLaM?";
    String correcta = "1989";
    String incorrectaUno = "1995";
    String incorrectaDos = "1810";
    String incorrectaTres = "2001";

    TipoPregunta tipo = TipoPregunta.MULTIPLE_CHOICE;
    CategoriaPregunta categoria = CategoriaPregunta.HISTORIA;

    Provincia provincia = new Provincia();
    provincia.setNombre("Buenos Aires");

    Pregunta pregunta = new Pregunta(
      enunciado,
      correcta,
      incorrectaUno,
      incorrectaDos,
      incorrectaTres,
      tipo,
      categoria,
      provincia
    );

    assertThat(pregunta.getEnunciado(), is(equalTo(enunciado)));
    assertThat(pregunta.getRespuestaCorrecta(), is(equalTo(correcta)));
    assertThat(pregunta.getOpcionIncorrectaUno(), is(equalTo(incorrectaUno)));
    assertThat(pregunta.getTipoPregunta(), is(equalTo(tipo)));
    assertThat(pregunta.getCategoriaPregunta(), is(equalTo(categoria)));
    assertThat(pregunta.getProvincia(), is(notNullValue()));
    assertThat(pregunta.getProvincia().getNombre(), is(equalTo("Buenos Aires")));
  }

  @Test
  public void queSePuedanModificarLosValoresDeUnaPregunta() {
    Pregunta pregunta = new Pregunta();
    String nuevoEnunciado = "nuevo enunciado de prueba";

    pregunta.setEnunciado(nuevoEnunciado);
    pregunta.setCategoriaPregunta(CategoriaPregunta.GEOGRAFIA);

    assertThat(pregunta.getEnunciado(), is(equalTo(nuevoEnunciado)));
    assertThat(pregunta.getCategoriaPregunta(), is(equalTo(CategoriaPregunta.GEOGRAFIA)));
  }
}
