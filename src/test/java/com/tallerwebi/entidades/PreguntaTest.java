package com.tallerwebi.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
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

  @Test
  public void queSePuedaCrearUnaPreguntaConElConstructorCorto() {
    String enunciado = "¿Cuál es la capital de Argentina?";
    String respuestaCorrecta = "Buenos Aires";
    TipoPregunta tipo = TipoPregunta.MULTIPLE_CHOICE;
    CategoriaPregunta categoria = CategoriaPregunta.GEOGRAFIA;

    Provincia provincia = new Provincia();
    provincia.setNombre("Buenos Aires");

    Pregunta pregunta = new Pregunta(enunciado, respuestaCorrecta, tipo, categoria, provincia);

    assertThat(pregunta.getEnunciado(), is(equalTo(enunciado)));
    assertThat(pregunta.getRespuestaCorrecta(), is(equalTo(respuestaCorrecta)));
    assertThat(pregunta.getTipoPregunta(), is(equalTo(tipo)));
    assertThat(pregunta.getCategoriaPregunta(), is(equalTo(categoria)));
    assertThat(pregunta.getProvincia(), is(equalTo(provincia)));

    assertThat(pregunta.getOpcionIncorrectaUno(), is(nullValue()));
    assertThat(pregunta.getOpcionIncorrectaDos(), is(nullValue()));
    assertThat(pregunta.getOpcionIncorrectaTres(), is(nullValue()));
  }

  @Test
  public void queSePuedanSetearYObtenerTodosLosCamposDeLaPregunta() {
    Pregunta pregunta = new Pregunta();

    Provincia provincia = new Provincia();
    provincia.setNombre("Córdoba");

    pregunta.setId(10L);
    pregunta.setEnunciado("¿Cuál es la provincia mediterránea?");
    pregunta.setRespuestaCorrecta("Córdoba");
    pregunta.setOpcionIncorrectaUno("Mendoza");
    pregunta.setOpcionIncorrectaDos("Santa Fe");
    pregunta.setOpcionIncorrectaTres("Chubut");
    pregunta.setTipoPregunta(TipoPregunta.MULTIPLE_CHOICE);
    pregunta.setCategoriaPregunta(CategoriaPregunta.GEOGRAFIA);
    pregunta.setProvincia(provincia);

    assertThat(pregunta.getId(), is(equalTo(10L)));
    assertThat(pregunta.getEnunciado(), is(equalTo("¿Cuál es la provincia mediterránea?")));
    assertThat(pregunta.getRespuestaCorrecta(), is(equalTo("Córdoba")));
    assertThat(pregunta.getOpcionIncorrectaUno(), is(equalTo("Mendoza")));
    assertThat(pregunta.getOpcionIncorrectaDos(), is(equalTo("Santa Fe")));
    assertThat(pregunta.getOpcionIncorrectaTres(), is(equalTo("Chubut")));
    assertThat(pregunta.getTipoPregunta(), is(equalTo(TipoPregunta.MULTIPLE_CHOICE)));
    assertThat(pregunta.getCategoriaPregunta(), is(equalTo(CategoriaPregunta.GEOGRAFIA)));
    assertThat(pregunta.getProvincia(), is(equalTo(provincia)));
  }

  @Test
  public void getOpcionesMezcladasDebeRetornarRespuestaCorrectaYOpcionesIncorrectas() {
    Pregunta pregunta = new Pregunta(
      "¿Cuál es la capital de Mendoza?",
      "Mendoza",
      "San Rafael",
      "Godoy Cruz",
      "Maipú",
      TipoPregunta.MULTIPLE_CHOICE,
      CategoriaPregunta.GEOGRAFIA,
      new Provincia()
    );

    List<String> opciones = pregunta.getOpcionesMezcladas();

    assertThat(opciones, hasSize(4));
    assertThat(opciones, containsInAnyOrder("Mendoza", "San Rafael", "Godoy Cruz", "Maipú"));
  }

  @Test
  public void getOpcionesMezcladasDebeCrearUnaListaNuevaCadaVez() {
    Pregunta pregunta = new Pregunta(
      "¿Cuál es la capital de Santa Fe?",
      "Santa Fe",
      "Rosario",
      "Rafaela",
      "Venado Tuerto",
      TipoPregunta.MULTIPLE_CHOICE,
      CategoriaPregunta.GEOGRAFIA,
      new Provincia()
    );

    List<String> primerasOpciones = pregunta.getOpcionesMezcladas();
    List<String> segundasOpciones = pregunta.getOpcionesMezcladas();

    assertThat(primerasOpciones, hasSize(4));
    assertThat(segundasOpciones, hasSize(4));

    assertThat(
      primerasOpciones,
      containsInAnyOrder("Santa Fe", "Rosario", "Rafaela", "Venado Tuerto")
    );

    assertThat(
      segundasOpciones,
      containsInAnyOrder("Santa Fe", "Rosario", "Rafaela", "Venado Tuerto")
    );
  }
}
