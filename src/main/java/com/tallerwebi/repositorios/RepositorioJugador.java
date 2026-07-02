package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Jugador;
import java.util.List;

public interface RepositorioJugador {
  void guardar(Jugador jugador);

  void actualizar(Jugador jugador);

  void eliminarTodos();

  List<Jugador> buscarTodos();

  Jugador buscarPorId(Long idJugadorDuenio);

  Jugador buscarPorUsuarioIdYPartidaId(Long usuarioId, Long partidaId);
}
