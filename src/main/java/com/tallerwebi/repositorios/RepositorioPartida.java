package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Partida;

public interface RepositorioPartida {
  void guardar(Partida partida);

  Partida buscarPorId(Long id);

  void actualizar(Partida partida);
}
