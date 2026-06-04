package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.ServicioUsuario;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

  private RepositorioUsuario repositorioUsuario;
  private final RepositorioRol repositorioRol;

  @Autowired
  public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioRol repositorioRol) {
    this.repositorioUsuario = repositorioUsuario;
    this.repositorioRol = repositorioRol;
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

  @Override
  public void cargarRolesIniciales() {
    crearRolSiNoExiste("JUGADOR");
    crearRolSiNoExiste("EDITOR");
    crearRolSiNoExiste("ADMIN");
  }

  @Override
  public void cargarUsuarioAdminInicial() {
    Usuario adminExistente = repositorioUsuario.buscarUsuarioPorEmail("admin@triviador.com");
    if (adminExistente != null) {
      return;
    }

    Rol rolAdmin = repositorioRol.buscarPorDescripcion("ADMIN");

    Usuario admin = new Usuario();
    admin.setEmail("admin@triviador.com");
    admin.setPassword("admin123");
    admin.setNombre("Administrador");
    admin.setNombreJugador("Admin");
    admin.setRol(rolAdmin);

    repositorioUsuario.crearUsuario(admin);
  }

  private void crearRolSiNoExiste(String descripcion) {
    Rol rolExistente = repositorioRol.buscarPorDescripcion(descripcion);
    if (rolExistente == null) {
      repositorioRol.guardar(new Rol(descripcion));
    }
  }
}
