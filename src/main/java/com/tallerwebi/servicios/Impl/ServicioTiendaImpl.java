package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioComodin;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.ServicioTienda;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioTiendaImpl implements ServicioTienda {

  private final RepositorioComodin repositorioComodin;
  private final RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioTiendaImpl(
    RepositorioComodin repositorioComodin,
    RepositorioUsuario repositorioUsuario
  ) {
    this.repositorioComodin = repositorioComodin;
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public void procesarCompraDeComodin(Long idUsuario, String nombreComodin) {
    final Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
    if (usuario == null) {
      throw new IllegalArgumentException("Usuario no encontrado.");
    }

    final Comodin comodin = repositorioComodin.buscarPorNombre(nombreComodin);
    if (comodin == null) {
      throw new IllegalArgumentException("El comodín solicitado no existe.");
    }

    usuario.adquirirComodin(comodin);
    repositorioUsuario.actualizarUsuario(usuario);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Comodin> obtenerCatalogoDeComodines() {
    return repositorioComodin.buscarTodos();
  }
}
