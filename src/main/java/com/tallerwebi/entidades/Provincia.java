package com.tallerwebi.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representa una provincia del mapa del juego.
 */

@Entity
@Table(name = "Provincia")
public class Provincia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String nombre;

  @Column(nullable = false)
  private Integer puntos = 0;

  @Column(name = "id_jugador_duenio")
  private Long idJugadorDuenio;

  protected Provincia() {
    // Constructor requerido por Hibernate.
  }

  public Provincia(String nombre, Integer puntos) {
    this.nombre = nombre;
    this.puntos = puntos;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public Integer getPuntos() {
    return puntos;
  }

  public Long getIdJugadorDuenio() {
    return idJugadorDuenio;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setPuntos(Integer puntos) {
    this.puntos = puntos;
  }

  public void setIdJugadorDuenio(Long idJugadorDuenio) {
    this.idJugadorDuenio = idJugadorDuenio;
  }
}
