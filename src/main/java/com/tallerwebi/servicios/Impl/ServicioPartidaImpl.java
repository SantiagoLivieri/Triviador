package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.EstadoDePartida;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.servicios.ServicioPartida;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {

  private final RepositorioPartida repositorioPartida;

  @Autowired
  public ServicioPartidaImpl(RepositorioPartida repositorioPartida) {
    this.repositorioPartida = repositorioPartida;
  }

  @Override
  public Partida crearPartida(List<Jugador> jugadores) {
    Partida partida = new Partida();

    List<Jugador> listaMezclada = new ArrayList<>(jugadores);

    Collections.shuffle(listaMezclada);

    partida.setJugadores(listaMezclada);
    partida.setJugadorEnTurno(listaMezclada.get(0));

    partida.setEtapaActual(1);
    partida.setInicioEtapa(LocalDateTime.now());

    repositorioPartida.guardar(partida);

    return partida;
  }

  @Override
  public Partida buscarPorId(Long partidaId) {
    return repositorioPartida.buscarPorId(partidaId);
  }

  @Override
  public void actualizar(Partida partida) {
    repositorioPartida.actualizar(partida);
  }

  @Override
  @Transactional
  public Partida buscarOCrearPartida(Usuario usuario) {
    Partida partida = repositorioPartida.buscarPartidaEnEspera();

    if (partida == null) {
      partida = new Partida();
      partida.setEstadoDePartida(EstadoDePartida.EN_ESPERA);
      partida.setJugadores(new ArrayList<>());
    }
    Jugador nuevojugador = new Jugador(usuario.getNombre(), "ROJO", usuario);

    partida.agregarJugador(nuevojugador);

    if (partida.getJugadores().size() == partida.CANTIDAD_JUGADORES_MAXIMA) {
      partida.setEstadoDePartida(EstadoDePartida.JUGANDO);
    }
    repositorioPartida.actualizar(partida);

    return partida;
  }

  @Override
  public int contarJugadoresEnPartida(Long idPartida) {
    Partida partida = repositorioPartida.buscarPorId(idPartida);
    return (partida != null) ? partida.getJugadores().size() : 0;
  }
}
