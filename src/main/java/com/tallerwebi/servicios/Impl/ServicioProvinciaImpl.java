package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.servicios.ServicioProvincia;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioProvinciaImpl implements ServicioProvincia {

  private final RepositorioProvincia repositorioProvincia;

  public ServicioProvinciaImpl(RepositorioProvincia repositorioProvincia) {
    this.repositorioProvincia = repositorioProvincia;
  }

  @Override
  public Provincia obtenerProvinciaPorId(Long idProvincia) {
    return repositorioProvincia.buscarPorId(idProvincia);
  }

  @Override
  public List<Provincia> obtenerProvincias() {
    return repositorioProvincia.buscarTodas();
  }

  @Override
  public void resetearProvincias() {
    repositorioProvincia.resetearProvincias();
  }

  @Override
  public void actualizar(Provincia provincia) {
    // TODO Auto-generated method stub
    repositorioProvincia.actualizar(provincia);
  }
}
