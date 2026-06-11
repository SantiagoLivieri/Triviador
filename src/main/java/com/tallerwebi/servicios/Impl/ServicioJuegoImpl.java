package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioJugador;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

  private final ServicioJugador servicioJugador;
  private final ServicioProvincia servicioProvincia;
  private final ServicioPregunta servicioPregunta;
  private final RepositorioPartida repositorioPartida;
  private static final int TIEMPO_MAXIMO_TURNO = 30;

  @Autowired
  public ServicioJuegoImpl(
    ServicioJugador servicioJugador,
    RepositorioPartida repositorioPartida,
    ServicioProvincia servicioProvincia,
    ServicioPregunta servicioPregunta
  ) {
    this.servicioJugador = servicioJugador;
    this.repositorioPartida = repositorioPartida;
    this.servicioPregunta = servicioPregunta;
    this.servicioProvincia = servicioProvincia;
  }

  @Override
  public Long inicializarPartida(DatosLobby datosLobby) {
    servicioProvincia.resetearProvincias();

    Jugador j1 = new Jugador(datosLobby.getNombreJugadorUno(), datosLobby.getColorJugadorUno());
    Jugador j2 = new Jugador(datosLobby.getNombreJugadorDos(), datosLobby.getColorJugadorDos());
    Jugador j3 = new Jugador(datosLobby.getNombreJugadorTres(), datosLobby.getColorJugadorTres());

    servicioJugador.guardar(j1);
    servicioJugador.guardar(j2);
    servicioJugador.guardar(j3);

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
  public Boolean procesarRespuestaYPasarTurno(
    Long partidaId,
    Long idProvincia,
    Long idPregunta,
    String respuesta
  ) {
    Partida partida = repositorioPartida.buscarPorId(partidaId);
    Pregunta pregunta = servicioPregunta.buscarPorId(idPregunta);

    boolean acerto = pregunta.getRespuestaCorrecta().trim().equalsIgnoreCase(respuesta.trim());

    if (acerto) {
      Jugador jugadorActual = partida.getJugadorEnTurno();
      Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

      // Asignación de dueño y puntaje SOLO para provincias neutrales
      // La asignación para conquistas ahora se hace en concretarConquista()
      // Cambio hecho para evitar bugs por lógica duplicada
      boolean provinciaNeutral = provincia.getIdJugadorDuenio() == null;
      if (provinciaNeutral) {
        Integer puntajeActual = (jugadorActual.getPuntaje() != null)
          ? jugadorActual.getPuntaje()
          : 0;

        jugadorActual.setPuntaje(puntajeActual + 20);
        provincia.setPuntos(20);

        provincia.setIdJugadorDuenio(jugadorActual.getId());
        servicioProvincia.actualizar(provincia);
      }
      servicioJugador.actualizar(jugadorActual);
    } else {
      avanzarTurno(partida);
    }
    return acerto;
  }

  /*Checa si la prov atacada tiene dueño. Si tiene: 3 preguntas / No tiene: 1pregunta */
  @Override
  public Integer obtenerCantidadPreguntasRequeridas(Long idProvincia) {
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);
    if (provincia.getIdJugadorDuenio() == null) {
      return 1;
    }
    return 3;
  }

  //Impide atacar provincias propias
  @Override
  public void validarAtaque(Long jugadorId, Long idProvincia) {
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);
    if (
      provincia != null &&
      provincia.getIdJugadorDuenio() != null &&
      provincia.getIdJugadorDuenio().equals(jugadorId)
    ) {
      throw new IllegalArgumentException("No podes atacar tu propia provincia, ¡Ataca otra!");
    }
  }

  //Determina si se respondieron la cantidad de preguntas necesarias
  @Override
  public boolean disputaFinalizada(Integer respondidas, Integer requeridas) {
    return respondidas >= requeridas;
  }

  
  @Override
  public boolean esConquista(Integer preguntasRequeridas) {
    return preguntasRequeridas == 3;
  }

  //Se ocupa de cambio de dueño y puntaje en conquistas
  @Override
  public void concretarConquista(Long partidaId, Long idProvincia) {
    Partida partida = repositorioPartida.buscarPorId(partidaId);
    Jugador jugadorActual = partida.getJugadorEnTurno();
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    Jugador exduenio = servicioJugador.buscarPorId(provincia.getIdJugadorDuenio());

    Integer puntajeExDuenio = (exduenio.getPuntaje() != null) ? exduenio.getPuntaje() : 0;
    exduenio.setPuntaje(Math.max(0, puntajeExDuenio - 10));
    servicioJugador.actualizar(exduenio);

    Integer puntajeInvasor = (jugadorActual.getPuntaje() != null) ? jugadorActual.getPuntaje() : 0;
    jugadorActual.setPuntaje(puntajeInvasor + 50);

    provincia.setPuntos(50);
    provincia.setIdJugadorDuenio(jugadorActual.getId());

    servicioProvincia.actualizar(provincia);
    servicioJugador.actualizar(jugadorActual);
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
}