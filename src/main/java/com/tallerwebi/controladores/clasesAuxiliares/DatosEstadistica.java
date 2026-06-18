package com.tallerwebi.controladores.clasesAuxiliares;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DatosEstadistica {

  public static final int PUESTO_PRIMERO = 1;
  public static final int PUESTO_SEGUNDO = 2;
  public static final int XP_PRIMER_PUESTO = 200;
  public static final int XP_SEGUNDO_PUESTO = 100;
  public static final int XP_TERCER_PUESTO = -100;

  @Column(nullable = false)
  private Integer experiencia = 0;

  @Column(name = "partidas_jugadas", nullable = false)
  private Integer partidasJugadas = 0;

  @Column(name = "partidas_ganadas", nullable = false)
  private Integer partidasGanadas = 0;

  public DatosEstadistica() {}

  public void registrarFinDePartida(Integer puesto, Integer xpGanada) {
    this.partidasJugadas = (this.partidasJugadas == null) ? 1 : this.partidasJugadas + 1;

    int baseXp = (this.experiencia == null) ? 0 : this.experiencia;
    int nuevaXp = baseXp + xpGanada;
    this.experiencia = Math.max(0, nuevaXp);

    if (puesto == PUESTO_PRIMERO) {
      this.partidasGanadas = (this.partidasGanadas == null) ? 1 : this.partidasGanadas + 1;
    }
  }

  public int calcularXPSegunPuesto(int puesto) {
    if (puesto == PUESTO_PRIMERO) {
      return XP_PRIMER_PUESTO;
    }
    if (puesto == PUESTO_SEGUNDO) {
      return XP_SEGUNDO_PUESTO;
    }
    return XP_TERCER_PUESTO;
  }

  public int getNivelActual() {
    if (this.experiencia == null || this.experiencia < 200) {
      return 1;
    }

    return (this.experiencia / 200) + 1;
  }

  public Integer getExperiencia() {
    return experiencia;
  }

  public void setExperiencia(Integer experiencia) {
    this.experiencia = experiencia;
  }

  public Integer getPartidasJugadas() {
    return partidasJugadas;
  }

  public void setPartidasJugadas(Integer partidasJugadas) {
    this.partidasJugadas = partidasJugadas;
  }

  public Integer getPartidasGanadas() {
    return partidasGanadas;
  }

  public void setPartidasGanadas(Integer partidasGanadas) {
    this.partidasGanadas = partidasGanadas;
  }
}
