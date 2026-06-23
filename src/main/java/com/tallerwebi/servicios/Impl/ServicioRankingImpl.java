package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.ServicioRanking;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioRankingImpl implements ServicioRanking {

  private final RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioRankingImpl(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> obtenerTop10General() {
    return repositorioUsuario.obtenerTop10Historico();
  }

  @Override
  @Transactional(readOnly = true)
  public Long calcularPuestoUsuario(Usuario usuario) {
    if (usuario == null) {
      return 0L;
    }

    final Integer experienciaActual = usuario.getExperiencia();

    final String nombreActual = (usuario.getNombreJugador() != null)
      ? usuario.getNombreJugador()
      : "";

    final Long cantidadMejores = repositorioUsuario.obtenerPosicionEnRanking(
      experienciaActual,
      nombreActual
    );

    final Long puestoUsuario = (cantidadMejores != null) ? cantidadMejores : 0L;

    return puestoUsuario + 1L;
  }
}
