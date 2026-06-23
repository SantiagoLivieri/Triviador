package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HistorialPartida")
public class HistorialPartida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "fecha_finalizacion", nullable = false)
  private LocalDateTime fechaFinalizacion;

  @Column(name = "puesto_final", nullable = false)
  private Integer puestoFinal;

  @Column(name = "experiencia_ganada", nullable = false)
  private Integer experienciaGanada;

  @Column(name = "nombre_ganador", length = 100, nullable = false)
  private String nombreGanador;

  protected HistorialPartida() {}

  public HistorialPartida(
    Usuario usuario,
    Integer puestoFinal,
    Integer experienciaGanada,
    String nombreGanador
  ) {
    this.usuario = usuario;
    this.puestoFinal = puestoFinal;
    this.experienciaGanada = experienciaGanada;
    this.nombreGanador = nombreGanador;
    this.fechaFinalizacion = LocalDateTime.now();
  }

  public String getFechaFormateada() {
    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(
      "dd/MM/yyyy HH:mm"
    );
    return this.fechaFinalizacion.format(formatter);
  }

  public Long getId() {
    return id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public LocalDateTime getFechaFinalizacion() {
    return fechaFinalizacion;
  }

  public Integer getPuestoFinal() {
    return puestoFinal;
  }

  public Integer getExperienciaGanada() {
    return experienciaGanada;
  }

  public String getNombreGanador() {
    return nombreGanador;
  }
}
