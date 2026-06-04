package com.tallerwebi.servicios.Impl;

import com.tallerwebi.servicios.ServicioCargaInicial;
import com.tallerwebi.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioCargaInicialImpl implements ServicioCargaInicial {

  private final ServicioUsuario servicioUsuario;

  @Autowired
  public ServicioCargaInicialImpl(ServicioUsuario servicioUsuario) {
    this.servicioUsuario = servicioUsuario;
  }

  @Override
  public void cargarDatosIniciales() {
    servicioUsuario.cargarRolesIniciales();
    servicioUsuario.cargarUsuarioAdminInicial();
  }
}
