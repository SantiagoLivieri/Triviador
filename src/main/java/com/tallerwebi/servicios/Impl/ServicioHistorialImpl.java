package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.repositorios.RepositorioHistorial;
import com.tallerwebi.servicios.ServicioHistorial;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioHistorial")
@Transactional
public class ServicioHistorialImpl implements ServicioHistorial {

  private final RepositorioHistorial repositorioHistorial;

  @Autowired
  public ServicioHistorialImpl(RepositorioHistorial repositorioHistorial) {
    this.repositorioHistorial = repositorioHistorial;
  }

  @Override
  public List<HistorialPartida> buscarHistorialPorUsuario(Long usuarioId) {
    return repositorioHistorial.buscarPorUsuarioId(usuarioId);
  }

  @Override
  public void guardar(HistorialPartida historial) {
    repositorioHistorial.guardar(historial);
  }
}
