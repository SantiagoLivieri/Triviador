package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioJugador;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

  private final RepositorioJugador repositorioJugador;
  private final RepositorioProvincia repositorioProvincia;
  private final RepositorioPregunta repositorioPregunta;
  private final RepositorioPartida repositorioPartida;
  private static final int TIEMPO_MAXIMO_TURNO = 30;

  @Autowired
  public ServicioJuegoImpl(
    RepositorioJugador repositorioJugador,
    RepositorioProvincia repositorioProvincia,
    RepositorioPregunta repositorioPregunta,
    RepositorioPartida repositorioPartida
  ) {
    this.repositorioJugador = repositorioJugador;
    this.repositorioProvincia = repositorioProvincia;
    this.repositorioPregunta = repositorioPregunta;
    this.repositorioPartida = repositorioPartida;
  }

  @Override
  public Long inicializarPartida(DatosLobby datosLobby) {
    crearProvinciasSiNoExisten();
    repositorioProvincia.resetearProvincias();

    Jugador j1 = new Jugador(datosLobby.getNombreJugadorUno(), datosLobby.getColorJugadorUno());
    Jugador j2 = new Jugador(datosLobby.getNombreJugadorDos(), datosLobby.getColorJugadorDos());
    Jugador j3 = new Jugador(datosLobby.getNombreJugadorTres(), datosLobby.getColorJugadorTres());

    repositorioJugador.guardar(j1);
    repositorioJugador.guardar(j2);
    repositorioJugador.guardar(j3);

    Partida partida = new Partida();
    partida.setJugadores(Arrays.asList(j1, j2, j3));
    partida.setJugadorEnTurno(j1);
    partida.setEtapaActual(1);
    partida.setInicioEtapa(LocalDateTime.now());

    repositorioPartida.guardar(partida);
    return partida.getId();
  }

  @Override
  public Partida obtenerPartidaPorId(Long partidaId) {
    return repositorioPartida.buscarPorId(partidaId);
  }

  @Override
  public List<Jugador> obtenerJugadores() {
    return repositorioJugador.buscarTodos();
  }

  @Override
  public Provincia obtenerProvinciaPorId(Long idProvincia) {
    return repositorioProvincia.buscarPorId(idProvincia);
  }

  @Override
  public List<Provincia> obtenerProvincias() {
    crearProvinciasSiNoExisten();
    return repositorioProvincia.buscarTodas();
  }

  @Override
  public List<String> obtenerOpcionesMezcladas(Pregunta pregunta) {
    List<String> opciones = new ArrayList<>();
    opciones.add(pregunta.getRespuestaCorrecta());
    opciones.add(pregunta.getOpcionIncorrectaUno());
    opciones.add(pregunta.getOpcionIncorrectaDos());
    opciones.add(pregunta.getOpcionIncorrectaTres());
    Collections.shuffle(opciones);
    return opciones;
  }

  @Override
  public Boolean procesarRespuestaYPasarTurno(
    Long partidaId,
    Long idProvincia,
    Long idPregunta,
    String respuesta
  ) {
    Partida partida = repositorioPartida.buscarPorId(partidaId);
    Pregunta pregunta = repositorioPregunta.buscarPorId(idPregunta);

    boolean acerto = pregunta.getRespuestaCorrecta().trim().equalsIgnoreCase(respuesta.trim());

    if (acerto) {
      Jugador jugadorActual = partida.getJugadorEnTurno();
      Provincia provincia = repositorioProvincia.buscarPorId(idProvincia);

      if (
        provincia.getIdJugadorDuenio() != null &&
        !provincia.getIdJugadorDuenio().equals(jugadorActual.getId())
      ) {
        Jugador exduenio = repositorioJugador.buscarPorId(provincia.getIdJugadorDuenio());

        Integer puntajeExDuenio = (exduenio.getPuntaje() != null) ? exduenio.getPuntaje() : 0;
        exduenio.setPuntaje(Math.max(0, puntajeExDuenio - 10));
        repositorioJugador.actualizar(exduenio);

        Integer puntajeInvasor = (jugadorActual.getPuntaje() != null)
          ? jugadorActual.getPuntaje()
          : 0;
        jugadorActual.setPuntaje(puntajeInvasor + 50);

        provincia.setPuntos(50);
      } else {
        Integer puntajeActual = (jugadorActual.getPuntaje() != null)
          ? jugadorActual.getPuntaje()
          : 0;
        jugadorActual.setPuntaje(puntajeActual + 20);
        provincia.setPuntos(20);
      }

      provincia.setIdJugadorDuenio(jugadorActual.getId());
      repositorioProvincia.actualizar(provincia);
      repositorioJugador.actualizar(jugadorActual);
    } else {
      avanzarTurno(partida);
    }
    return acerto;
  }

  @Override
  public void procesarJugada(Long partidaId, Long jugadorId, Long provinciaSeleccionadaId)
    throws TiempoAgotadoException, TurnoInvalidoException {
    Partida partida = repositorioPartida.buscarPorId(partidaId);

    if (!partida.getJugadorEnTurno().getId().equals(jugadorId)) {
      throw new TurnoInvalidoException("No es tu turno");
    }

    long segundosTranscurridos = ChronoUnit.SECONDS.between(
      partida.getInicioEtapa(),
      LocalDateTime.now()
    );
    if (segundosTranscurridos > TIEMPO_MAXIMO_TURNO) {
      avanzarTurno(partida);
      throw new TiempoAgotadoException("Se te acabó el tiempo");
    }

    partida.setEtapaActual(2);
    partida.setInicioEtapa(LocalDateTime.now());
    repositorioPartida.actualizar(partida);
  }

  @Override
  public void forzarSaltoPorTiempo(Long partidaId) {
    Partida partida = repositorioPartida.buscarPorId(partidaId);
    if (partida == null || partida.getInicioEtapa() == null) {
      return;
    }
    long segundosTranscurridos = ChronoUnit.SECONDS.between(
      partida.getInicioEtapa(),
      LocalDateTime.now()
    );

    if (segundosTranscurridos >= (TIEMPO_MAXIMO_TURNO - 2)) {
      avanzarTurno(partida);
    }
  }

  private void avanzarTurno(Partida partida) {
    List<Jugador> jugadores = partida.getJugadores();
    int indiceActual = jugadores.indexOf(partida.getJugadorEnTurno());

    int siguienteIndice = (indiceActual + 1) % jugadores.size();
    Jugador siguienteJugador = jugadores.get(siguienteIndice);

    partida.setJugadorEnTurno(siguienteJugador);
    partida.setEtapaActual(1);
    partida.setInicioEtapa(LocalDateTime.now());

    repositorioPartida.actualizar(partida);
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
  public Pregunta obtenerPreguntaPorProvincia(Long idProvincia) {
    List<Pregunta> preguntas = repositorioPregunta.buscarTodas();
    List<Pregunta> preguntasFiltradas = new ArrayList<>();
    for (Pregunta pregunta : preguntas) {
      if (pregunta.getProvincia() != null && pregunta.getProvincia().getId().equals(idProvincia)) {
        preguntasFiltradas.add(pregunta);
      }
    }
    if (preguntasFiltradas.isEmpty()) {
      if (preguntas.isEmpty()) {
        return null;
      }
      Collections.shuffle(preguntas);
      return preguntas.get(0);
    }
    Collections.shuffle(preguntasFiltradas);
    return preguntasFiltradas.get(0);
  }
}
