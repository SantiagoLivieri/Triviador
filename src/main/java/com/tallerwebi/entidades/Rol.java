package com.tallerwebi.entidades;

import javax.persistence.*;

@Entity
public class Rol {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descripcion;

  public Rol(String descripcion) {
    this.descripcion = descripcion;
  }

  public Rol() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
