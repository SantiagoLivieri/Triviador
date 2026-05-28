package com.tallerwebi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CargaInicialAlIniciar implements ApplicationListener<ContextRefreshedEvent> {

  private final ServicioCargaInicial servicioCargaInicial;

  private boolean cargaRealizada;

  @Autowired
  public CargaInicialAlIniciar(ServicioCargaInicial servicioCargaInicial) {
    this.servicioCargaInicial = servicioCargaInicial;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!cargaRealizada) {
      servicioCargaInicial.cargarDatosIniciales();
      cargaRealizada = true;
    }
  }
}
