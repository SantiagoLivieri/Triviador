package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
  private Integer rondaActual = 1;
  private static final Integer CANTIDAD_MAX_RONDAS = 12;
  private Integer conquistasEnEsteTurno = 0;
  private static final int MAX_CONQUISTAS_POR_TURNO = 3;
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

    if (!estaFinalizada()) {
      int indiceActual = this.jugadores.indexOf(this.jugadorEnTurno);

      if (indiceActual == this.jugadores.size() - 1) {
        this.rondaActual++;
      }

      if (estaFinalizada()) {
        return;
      }
      int siguienteIndice = (indiceActual + 1) % this.jugadores.size();

      this.jugadorEnTurno = this.jugadores.get(siguienteIndice);
      this.etapaActual = 1;
      this.inicioEtapa = LocalDateTime.now();
      this.conquistasEnEsteTurno = 0;
    }
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
    return rondaActual != null ? rondaActual : 1;
  }

  public void setRondaActual(Integer rondaActual) {
    this.rondaActual = rondaActual;
  }

  public boolean estaFinalizada() {
    if (this.rondaActual == null) {
      this.rondaActual = 0;
    }
    return this.getRondaActual() > CANTIDAD_MAX_RONDAS;
  }

  public List<Jugador> obtenerRanking() {
    List<Jugador> ranking = new ArrayList<>(this.jugadores);
    ranking.sort((j1, j2) -> Integer.compare(j2.getPuntaje(), j1.getPuntaje()));
    return ranking;
  }

  public void registrarConquista() {
    if (this.conquistasEnEsteTurno == null) {
      this.conquistasEnEsteTurno = 0;
    }
    this.conquistasEnEsteTurno++;
  }

  public boolean alcanzoLimiteConquistas() {
    return (
      this.conquistasEnEsteTurno != null && this.conquistasEnEsteTurno >= MAX_CONQUISTAS_POR_TURNO
    );
  }
}
