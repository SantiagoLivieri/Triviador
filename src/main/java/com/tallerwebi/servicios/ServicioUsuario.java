package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface ServicioUsuario {
  Usuario buscarUsuarioPorId(Long id);

  void actualizarPerfil(Long id, String nombre, String nombreJugador);

  void cargarRolesIniciales();

  void cargarUsuarioAdminInicial();

  void actualizarUsuario(Usuario usuario);

  void procesarCompraDeComodin(Long idUsuario, String nombreComodin);

  List<Comodin> obtenerCatalogoDeComodines();
}
