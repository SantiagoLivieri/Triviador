package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.servicios.ServicioPartida;
import java.time.LocalDateTime;
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

    partida.setJugadores(jugadores);
    partida.setJugadorEnTurno(jugadores.get(0));
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
}
