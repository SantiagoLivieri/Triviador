package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioUsuario;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Usuario buscarUsuarioPorId(Long id) {
    return repositorioUsuario.buscarUsuarioPorId(id);
  }

  @Override
  public void actualizarPerfil(Long id, String nombre, String nombreJugador) {
    Usuario usuario = repositorioUsuario.buscarUsuarioPorId(id);

    usuario.setNombre(nombre);
    usuario.setNombreJugador(nombreJugador);

    repositorioUsuario.actualizarUsuario(usuario);
  }
}
