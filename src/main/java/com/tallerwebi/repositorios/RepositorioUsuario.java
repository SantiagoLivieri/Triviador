package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Usuario;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String password);

  Usuario buscarUsuarioPorEmail(String email);

  void crearUsuario(Usuario usuario);
}
