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
  public Provincia buscarPorId(Long idProvincia) {
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
    if (provincia == null || provincia.getId() == null) {
      throw new IllegalArgumentException(
        "No se puede actualizar una provincia nula o sin ID asignado."
      );
    }
    repositorioProvincia.actualizar(provincia);
  }

  @Override
  public Integer obtenerCantidadPreguntasRequeridas(Long idProvincia) {
    Provincia provincia = buscarPorId(idProvincia);

    if (provincia == null) {
      throw new IllegalArgumentException("La provincia solicitada no existe");
    }

    return provincia.getCantidadPreguntasRequeridas();
  }
}
