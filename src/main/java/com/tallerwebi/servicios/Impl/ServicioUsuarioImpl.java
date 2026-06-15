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

  private final RepositorioUsuario repositorioUsuario;
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

    if (usuario == null) {
      throw new IllegalArgumentException(
        "No se puede actualizar el perfil: El usuario con ID " + id + " no existe"
      );
    }

    if (
      nombre == null ||
      nombre.trim().isEmpty() ||
      nombreJugador == null ||
      nombreJugador.trim().isEmpty()
    ) {
      throw new IllegalArgumentException("El nombre y el nombre de jugador no pueden estar vacios");
    }

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
    if (rolAdmin == null) {
      throw new IllegalStateException(
        "No se puede inicializar el Administrador: El rol ADMIN no existe"
      );
    }

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
