package com.tallerwebi.entidades;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PartidaTest {

    @Test
    public void queSePuedaCrearUnaPartidaVacia() {

        Partida partida = new Partida();

        assertThat(partida, is(notNullValue()));
        assertThat(partida.getId(), is(nullValue())); 
        assertThat(partida.getEtapaActual(), is(nullValue()));
    }

    @Test
    public void queSePuedanAsignarYObtenerJugadores() {
        
        Partida partida = new Partida();
        List<Jugador> jugadores = new ArrayList<>();
        Jugador jugador1 = new Jugador();
        Jugador jugador2 = new Jugador();
        jugadores.add(jugador1);
        jugadores.add(jugador2);

       
        partida.setJugadores(jugadores);

 
        assertThat(partida.getJugadores(), hasSize(2));
        assertThat(partida.getJugadores(), contains(jugador1, jugador2));
    }

    @Test
    public void queSePuedaAsignarYObtenerElJugadorEnTurno() {
   
        Partida partida = new Partida();
        Jugador jugadorDeTurno = new Jugador();

 
        partida.setJugadorEnTurno(jugadorDeTurno);


        assertThat(partida.getJugadorEnTurno(), is(equalTo(jugadorDeTurno)));
    }

    @Test
    public void queSePuedaModificarLaEtapaActual() {
     
        Partida partida = new Partida();
        Integer etapaDeseada = 2;

        partida.setEtapaActual(etapaDeseada);

        assertThat(partida.getEtapaActual(), is(equalTo(etapaDeseada)));
    }

    @Test
    public void queSePuedaRegistrarElInicioDeLaEtapa() {
   
        Partida partida = new Partida();
        LocalDateTime ahora = LocalDateTime.now();

        partida.setInicioEtapa(ahora);

        assertThat(partida.getInicioEtapa(), is(equalTo(ahora)));
    }
}
