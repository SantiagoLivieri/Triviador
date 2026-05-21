package com.tallerwebi.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representa un jugador dentro de una partida local.
 */
@Entity
@Table(name = "Jugador")
public class Jugador {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 50)
  private String color;

  @Column(nullable = false)
  private Integer puntaje = 0;

  public Jugador() {}

  public Jugador(String nombre, String color) {
    this.nombre = nombre;
    this.color = color;
    this.puntaje = 0;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getColor() {
    return color;
  }

  public Integer getPuntaje() {
    return puntaje;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setPuntaje(Integer puntaje) {
    this.puntaje = puntaje;
  }
}
