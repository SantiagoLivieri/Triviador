package com.tallerwebi.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PaqueteMonedas {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String titulo;

  private Integer cantidadCoins;
  private Double precioArs;
  private String beneficioExtra;

  public PaqueteMonedas() {}

  public PaqueteMonedas(
    String titulo,
    Integer cantidadCoins,
    Double precioArs,
    String beneficioExtra
  ) {
    this.titulo = titulo;
    this.cantidadCoins = cantidadCoins;
    this.precioArs = precioArs;
    this.beneficioExtra = beneficioExtra;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Integer getCantidadCoins() {
    return cantidadCoins;
  }

  public void setCantidadCoins(Integer cantidadCoins) {
    this.cantidadCoins = cantidadCoins;
  }

  public Double getPrecioArs() {
    return precioArs;
  }

  public void setPrecioArs(Double precioArs) {
    this.precioArs = precioArs;
  }

  public String getBeneficioExtra() {
    return beneficioExtra;
  }

  public void setBeneficioExtra(String beneficioExtra) {
    this.beneficioExtra = beneficioExtra;
  }
}
