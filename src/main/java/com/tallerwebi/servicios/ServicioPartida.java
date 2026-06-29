package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface ServicioPartida {
  public Partida crearPartida(List<Jugador> jugadores);

  public Partida buscarPorId(Long partidaId);

  public void actualizar(Partida partida);

  public Partida buscarOCrearPartida(Usuario usuario);

  int contarJugadoresEnPartida(Long idPartida);
}
