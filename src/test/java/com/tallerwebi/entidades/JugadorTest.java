package com.tallerwebi.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class JugadorTest {

  @Test
  public void queSePuedaCrearUnJugadorConConstructorVacioYSuPuntajeSeaCero() {
    Jugador jugador = new Jugador();

    assertThat(jugador, is(notNullValue()));
    assertThat(jugador.getId(), is(nullValue()));
    assertThat(jugador.getNombre(), is(nullValue()));
    assertThat(jugador.getColor(), is(nullValue()));
    assertThat(jugador.getPuntaje(), is(equalTo(0)));
  }

  @Test
  public void queSePuedaCrearUnJugador() {
    String nombre = "juan";
    String color = "celeste";

    Jugador jugador = new Jugador(nombre, color);

    assertThat(jugador.getNombre(), is(equalTo(nombre)));
    assertThat(jugador.getColor(), is(equalTo(color)));
    assertThat(jugador.getPuntaje(), is(equalTo(0)));
  }

  @Test
  public void queSePuedanModificarLosAtributosDeUnJugador() {
    Jugador jugador = new Jugador();

    jugador.setNombre("marcos");
    jugador.setColor("rojo");
    jugador.setPuntaje(150);

    assertThat(jugador.getNombre(), is(equalTo("marcos")));
    assertThat(jugador.getColor(), is(equalTo("rojo")));
    assertThat(jugador.getPuntaje(), is(equalTo(150)));
  }
}
