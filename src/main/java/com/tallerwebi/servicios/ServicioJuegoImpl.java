package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioJugador;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioProvincia;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion del servicio principal del juego.
 */
@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

  private final RepositorioJugador repositorioJugador;
  private final RepositorioProvincia repositorioProvincia;
  private final RepositorioPregunta repositorioPregunta;

  @Autowired
  public ServicioJuegoImpl(
    RepositorioJugador repositorioJugador,
    RepositorioProvincia repositorioProvincia,
    RepositorioPregunta repositorioPregunta
  ) {
    this.repositorioJugador = repositorioJugador;
    this.repositorioProvincia = repositorioProvincia;
    this.repositorioPregunta = repositorioPregunta;
  }

  @Override
  public void inicializarPartida(DatosLobby datosLobby) {
    crearProvinciasSiNoExisten();

    repositorioJugador.eliminarTodos();

    repositorioJugador.guardar(
      new Jugador(datosLobby.getNombreJugadorUno(), datosLobby.getColorJugadorUno())
    );

    repositorioJugador.guardar(
      new Jugador(datosLobby.getNombreJugadorDos(), datosLobby.getColorJugadorDos())
    );

    repositorioJugador.guardar(
      new Jugador(datosLobby.getNombreJugadorTres(), datosLobby.getColorJugadorTres())
    );

    repositorioProvincia.resetearProvincias();
  }

  @Override
  public List<Jugador> obtenerJugadores() {
    return repositorioJugador.buscarTodos();
  }

  @Override
  public List<Provincia> obtenerProvincias() {
    crearProvinciasSiNoExisten();
    return repositorioProvincia.buscarTodas();
  }

  private void crearProvinciasSiNoExisten() {
    if (repositorioProvincia.contar() > 0) {
      return;
    }

    List<String> nombresProvincias = Arrays.asList(
      "Buenos Aires",
      "CABA",
      "Catamarca",
      "Chaco",
      "Chubut",
      "Córdoba",
      "Corrientes",
      "Entre Ríos",
      "Formosa",
      "Jujuy",
      "La Pampa",
      "La Rioja",
      "Mendoza",
      "Misiones",
      "Neuquén",
      "Río Negro",
      "Salta",
      "San Juan",
      "San Luis",
      "Santa Cruz",
      "Santa Fe",
      "Santiago del Estero",
      "Tierra del Fuego",
      "Tucuman"
    );

    for (String nombre : nombresProvincias) {
      repositorioProvincia.guardar(new Provincia(nombre, 0));
    }
  }

  @Override
  public Jugador obtenerJugadorDelTurno(Integer turnoActual) {
    List<Jugador> jugadores = repositorioJugador.buscarTodos();

    if (jugadores.isEmpty()) {
      return null;
    }

    Integer turnoSeguro = turnoActual;

    if (turnoSeguro == null || turnoSeguro >= jugadores.size()) {
      turnoSeguro = 0;
    }

    return jugadores.get(turnoSeguro);
  }

  @Override
  public Pregunta obtenerPreguntaAleatoria() {
    List<Pregunta> preguntas = repositorioPregunta.buscarTodas();

    if (preguntas.isEmpty()) {
      return null;
    }

    Collections.shuffle(preguntas);
    return preguntas.get(0);
  }

  @Override
  public Boolean responderPregunta(
    Long idProvincia,
    Long idPregunta,
    String respuesta,
    Integer turnoActual
  ) {
    Pregunta pregunta = repositorioPregunta.buscarPorId(idPregunta);
    Provincia provincia = repositorioProvincia.buscarPorId(idProvincia);
    Jugador jugador = obtenerJugadorDelTurno(turnoActual);

    if (pregunta == null || provincia == null || jugador == null || respuesta == null) {
      return false;
    }

    Boolean respuestaCorrecta = respuesta
      .trim()
      .equalsIgnoreCase(pregunta.getRespuestaCorrecta().trim());

    if (respuestaCorrecta) {
      provincia.setIdJugadorDuenio(jugador.getId());
      provincia.setPuntos(10);

      jugador.setPuntaje(jugador.getPuntaje() + 10);

      repositorioProvincia.actualizar(provincia);
      repositorioJugador.actualizar(jugador);
    }

    return respuestaCorrecta;
  }
}
