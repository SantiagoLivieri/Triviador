package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Jugador;
import java.util.List;

public interface ServicioJugador {
  void guardar(Jugador jugador);

  void actualizar(Jugador jugador);

  List<Jugador> obtenerTodos();

  Jugador buscarPorId(Long idJugador);
  Jugador crearJugador(String nombre, String color);
}
