package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioJugador;
import com.tallerwebi.servicios.ServicioJugador;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJugadorImpl implements ServicioJugador {

  private final RepositorioJugador repositorioJugador;

  @Override
  public Jugador crearJugador(String nombre, String color) {
    Jugador jugador = new Jugador(nombre, color, null);

    repositorioJugador.guardar(jugador);

    return jugador;
  }

  @Override
  public Jugador crearJugadorConUsuario(Usuario usuario, String color) {
    Jugador jugador = new Jugador(usuario.getNombreJugador(), color, usuario);

    repositorioJugador.guardar(jugador);

    return jugador;
  }

  @Autowired
  public ServicioJugadorImpl(RepositorioJugador repositorioJugador) {
    this.repositorioJugador = repositorioJugador;
  }

  @Override
  public void guardar(Jugador jugador) {
    repositorioJugador.guardar(jugador);
  }

  @Override
  public void actualizar(Jugador jugador) {
    repositorioJugador.actualizar(jugador);
  }

  @Override
  public List<Jugador> obtenerTodos() {
    return repositorioJugador.buscarTodos();
  }

  @Override
  public Jugador buscarPorId(Long idJugador) {
    return repositorioJugador.buscarPorId(idJugador);
  }
}
