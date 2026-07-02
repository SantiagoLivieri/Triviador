package com.tallerwebi.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PagoProcesado {

  @Id
  private Long idMercadoPago;

  public PagoProcesado() {}

  public PagoProcesado(Long idMercadoPago) {
    this.idMercadoPago = idMercadoPago;
  }

  public Long getIdMercadoPago() {
    return idMercadoPago;
  }

  public void setIdMercadoPago(Long idMercadoPago) {
    this.idMercadoPago = idMercadoPago;
  }
}
