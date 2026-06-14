package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Partida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Jugador> jugadores;

  @ManyToOne
  private Jugador jugadorEnTurno;

  private Integer etapaActual;
  private Integer rondaActual;
  private LocalDateTime inicioEtapa;

  public Long getId() {
    return id;
  }

  public List<Jugador> getJugadores() {
    return jugadores;
  }

  public Jugador getJugadorEnTurno() {
    return jugadorEnTurno;
  }

  public Integer getEtapaActual() {
    return etapaActual;
  }

  public LocalDateTime getInicioEtapa() {
    return inicioEtapa;
  }

  public void setJugadorEnTurno(Jugador jugadorEnTurno) {
    this.jugadorEnTurno = jugadorEnTurno;
  }

  public void setEtapaActual(Integer etapaActual) {
    this.etapaActual = etapaActual;
  }

  public void setInicioEtapa(LocalDateTime inicioEtapa) {
    this.inicioEtapa = inicioEtapa;
  }

  public void setJugadores(List<Jugador> jugadores) {
    this.jugadores = jugadores;
  }

  public void avanzarTurno() {
    if (this.jugadores == null || this.jugadores.isEmpty()) {
      return;
    }
    int indiceActual = this.jugadores.indexOf(this.jugadorEnTurno);
    int siguienteIndice = (indiceActual + 1) % this.jugadores.size();

    if (siguienteIndice == 0) {
      if (this.rondaActual == null) {
        this.rondaActual = 1;
      }
      this.rondaActual++;
    }
    this.jugadorEnTurno = this.jugadores.get(siguienteIndice);
    this.etapaActual = 1;
    this.inicioEtapa = LocalDateTime.now();
  }

  public boolean tieneTiempoAgotado(int tiempoMaximoSegundos) {
    if (this.inicioEtapa == null) {
      return false;
    }
    long segundosTranscurridos = ChronoUnit.SECONDS.between(this.inicioEtapa, LocalDateTime.now());
    return segundosTranscurridos > tiempoMaximoSegundos;
  }

  public boolean esTurnoDe(Long jugadorId) {
    if (this.jugadorEnTurno == null || jugadorId == null) {
      return false;
    }

    return this.jugadorEnTurno.getId().equals(jugadorId);
  }

  public Integer getRondaActual() {
    return rondaActual;
  }

  public void setRondaActual(Integer rondaActual) {
    this.rondaActual = rondaActual;
  }
}
