package com.tallerwebi.entidades;

import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;

import javax.persistence.*;

@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private String email;
  private String password;

  @ManyToOne
  private Rol rol;

  private Boolean activo;

  public Usuario(DatosRegistro datosRegistro) {
    this.nombre = datosRegistro.getNombre();
    this.email = datosRegistro.getEmail();
    this.password = datosRegistro.getPassword();
    this.activo = true;
  }

  public Usuario() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public void activar() {
    activo = true;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }
}
