package com.tallerwebi.entidades;

import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representa un usuario o jugador del sistema.
 */
@Entity
@Table(name = "Usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private String email;

  private String password;

  private Boolean activo;

  @Column(name = "nombre_jugador", length = 100)
  private String nombreJugador;

  @Column(name = "color_asignado", length = 50)
  private String colorAsignado;

  @Column(nullable = false)
  private Integer puntaje = 0;

  @ManyToOne
  @JoinColumn(name = "rol_id")
  private Rol rol;

  public Usuario() {}

  public Usuario(DatosRegistro datosRegistro, Rol rolUser) {
    this.nombre = datosRegistro.getNombre();
    this.email = datosRegistro.getEmail();
    this.password = datosRegistro.getPassword();
    this.activo = true;
    this.rol = rolUser;
    this.nombreJugador = datosRegistro.getNombre();
    this.puntaje = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
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
    this.activo = true;
  }

  public String getNombreJugador() {
    return nombreJugador;
  }

  public void setNombreJugador(String nombreJugador) {
    this.nombreJugador = nombreJugador;
  }

  public String getColorAsignado() {
    return colorAsignado;
  }

  public void setColorAsignado(String colorAsignado) {
    this.colorAsignado = colorAsignado;
  }

  public Integer getPuntaje() {
    return puntaje;
  }

  public void setPuntaje(Integer puntaje) {
    this.puntaje = puntaje;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }
}
