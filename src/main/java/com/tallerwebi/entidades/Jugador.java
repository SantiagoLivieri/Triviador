package com.tallerwebi.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
  private Integer puntaje;

  //se relacionan varios jugadores a un mismo usuario por partida (porque es local).
  // De esta manera se pueden recopilar datos para el perfil.
  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = true)
  private Usuario usuario;

  protected Jugador() {}

  public Jugador(String nombre, String color, Usuario usuario) {
    this.usuario = usuario;

    if (usuario == null) {
      this.nombre = nombre;
    } else if (usuario.getNombreJugador() != null && !usuario.getNombreJugador().trim().isEmpty()) {
      this.nombre = usuario.getNombreJugador();
    } else {
      this.nombre = usuario.getNombre();
    }

    this.color = color;
    this.puntaje = 0;
  }

  public void sumarPuntos(Integer puntos) {
    if (this.puntaje == null) {
      this.puntaje = 0;
    }
    this.puntaje += puntos;
  }

  public void restarPuntos(Integer puntos) {
    this.puntaje = Math.max(0, this.puntaje - puntos);
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

  public Usuario getUsuario() {
    return usuario;
  }
}
