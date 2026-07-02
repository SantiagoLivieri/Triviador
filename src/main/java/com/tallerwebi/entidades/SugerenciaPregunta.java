package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("PMD.TooManyFields")
@Entity
@Table(name = "SugerenciaPregunta")
public class SugerenciaPregunta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String enunciado;

  private String respuestaCorrecta;

  private String opcionIncorrectaUno;

  private String opcionIncorrectaDos;

  private String opcionIncorrectaTres;

  @ManyToOne
  private Provincia provincia;

  @ManyToOne
  private Usuario usuarioCreador;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoSugerenciaPregunta estado = EstadoSugerenciaPregunta.PENDIENTE;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  protected SugerenciaPregunta() {
    // Constructor requerido por Hibernate.
  }

  public SugerenciaPregunta(
    String enunciado,
    String respuestaCorrecta,
    String opcionIncorrectaUno,
    String opcionIncorrectaDos,
    String opcionIncorrectaTres,
    Provincia provincia,
    Usuario usuarioCreador
  ) {
    this.enunciado = enunciado;
    this.respuestaCorrecta = respuestaCorrecta;
    this.opcionIncorrectaUno = opcionIncorrectaUno;
    this.opcionIncorrectaDos = opcionIncorrectaDos;
    this.opcionIncorrectaTres = opcionIncorrectaTres;
    this.provincia = provincia;
    this.usuarioCreador = usuarioCreador;
    this.estado = EstadoSugerenciaPregunta.PENDIENTE;
    this.fechaCreacion = LocalDateTime.now();
  }

  public void aprobar() {
    this.estado = EstadoSugerenciaPregunta.APROBADA;
  }

  public void rechazar() {
    this.estado = EstadoSugerenciaPregunta.RECHAZADA;
  }

  public void actualizarDatos(
    String enunciado,
    String respuestaCorrecta,
    String opcionIncorrectaUno,
    String opcionIncorrectaDos,
    String opcionIncorrectaTres,
    Provincia provincia
  ) {
    this.enunciado = enunciado;
    this.respuestaCorrecta = respuestaCorrecta;
    this.opcionIncorrectaUno = opcionIncorrectaUno;
    this.opcionIncorrectaDos = opcionIncorrectaDos;
    this.opcionIncorrectaTres = opcionIncorrectaTres;
    this.provincia = provincia;
  }

  public Long getId() {
    return id;
  }

  public String getEnunciado() {
    return enunciado;
  }

  public String getRespuestaCorrecta() {
    return respuestaCorrecta;
  }

  public String getOpcionIncorrectaUno() {
    return opcionIncorrectaUno;
  }

  public String getOpcionIncorrectaDos() {
    return opcionIncorrectaDos;
  }

  public String getOpcionIncorrectaTres() {
    return opcionIncorrectaTres;
  }

  public Provincia getProvincia() {
    return provincia;
  }

  public Usuario getUsuarioCreador() {
    return usuarioCreador;
  }

  public EstadoSugerenciaPregunta getEstado() {
    return estado;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setEnunciado(String enunciado) {
    this.enunciado = enunciado;
  }

  public void setRespuestaCorrecta(String respuestaCorrecta) {
    this.respuestaCorrecta = respuestaCorrecta;
  }

  public void setOpcionIncorrectaUno(String opcionIncorrectaUno) {
    this.opcionIncorrectaUno = opcionIncorrectaUno;
  }

  public void setOpcionIncorrectaDos(String opcionIncorrectaDos) {
    this.opcionIncorrectaDos = opcionIncorrectaDos;
  }

  public void setOpcionIncorrectaTres(String opcionIncorrectaTres) {
    this.opcionIncorrectaTres = opcionIncorrectaTres;
  }

  public void setProvincia(Provincia provincia) {
    this.provincia = provincia;
  }

  public void setUsuarioCreador(Usuario usuarioCreador) {
    this.usuarioCreador = usuarioCreador;
  }

  public void setEstado(EstadoSugerenciaPregunta estado) {
    this.estado = estado;
  }
}
