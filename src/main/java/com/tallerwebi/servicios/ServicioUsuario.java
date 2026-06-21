package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Usuario;

public interface ServicioUsuario {
  Usuario buscarUsuarioPorId(Long id);

  void actualizarPerfil(Long id, String nombre, String nombreJugador);

  void cargarRolesIniciales();

  void cargarUsuarioAdminInicial();

  void actualizarUsuario(Usuario usuario);
}
