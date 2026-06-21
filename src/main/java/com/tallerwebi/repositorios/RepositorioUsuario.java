package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String password);

  Usuario buscarUsuarioPorEmail(String email);

  Usuario buscarUsuarioPorId(Long id);

  void crearUsuario(Usuario usuario);

  void actualizarUsuario(Usuario usuario);

  List<Usuario> obtenerTop10Historico();

  Long obtenerPosicionEnRanking(Integer experiencia, String nombre);
}
