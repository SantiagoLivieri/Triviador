package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class Partida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "partida_id", nullable = false)
  @OrderColumn(name = "orden_turno")
  private List<Jugador> jugadores;

  @ManyToOne
  private Jugador jugadorEnTurno;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Long> preguntasHechas = new HashSet<>();

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
    if (this.jugadores == null) {
      return new ArrayList<>();
    }
    List<Jugador> jugadoresReales = new ArrayList<>();
    for (Jugador j : this.jugadores) {
      boolean existe = jugadoresReales.stream().anyMatch(u -> u.getId().equals(j.getId()));
      if (!existe) {
        jugadoresReales.add(j);
      }
    }
    return jugadoresReales;
  }

  public void avanzarTurno() {
    List<Jugador> jugadoresLimpios = this.getJugadores();

    if (jugadoresLimpios.isEmpty()) {
      return;
    }

    if (!estaFinalizada()) {
      int indiceActual = buscarIndiceJugadorActual(jugadoresLimpios);

      if (indiceActual == jugadoresLimpios.size() - 1) {
        this.rondaActual++;
      }

      if (estaFinalizada()) {
        return;
      }

      int siguienteIndice = (indiceActual + 1) % jugadoresLimpios.size();

      this.jugadorEnTurno = jugadoresLimpios.get(siguienteIndice);
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

  public Set<Long> getPreguntasHechas() {
    return preguntasHechas;
  }

  public void registrarPreguntaHecha(Long preguntaId) {
    this.preguntasHechas.add(preguntaId);
  }

  public void reiniciarPreguntasHechas() {
    this.preguntasHechas.clear();
  }

  private int buscarIndiceJugadorActual(List<Jugador> jugadoresLimpios) {
    for (int i = 0; i < jugadoresLimpios.size(); i++) {
      if (jugadoresLimpios.get(i).getId().equals(this.jugadorEnTurno.getId())) {
        return i;
      }
    }
    return 0;
  }

  public int calcularPuestoDeUsuario(Long usuarioId) {
    List<Jugador> ranking = this.obtenerRanking();

    for (int i = 0; i < ranking.size(); i++) {
      Jugador jugAnfitrion = ranking.get(i);
      if (
        jugAnfitrion.getUsuario() != null && jugAnfitrion.getUsuario().getId().equals(usuarioId)
      ) {
        return i + 1;
      }
    }

    return 3;
  }

  public String obtenerNombreGanador() {
    List<Jugador> ranking = this.obtenerRanking();
    return ranking.get(0).getNombre();
  }

  public Map<Long, String> obtenerMapaDeColoresPorJugador() {
    Map<Long, String> mapaColores = new HashMap<>();

    if (this.jugadores != null) {
      for (Jugador jug : this.jugadores) {
        if (jug != null && jug.getId() != null && jug.getColor() != null) {
          mapaColores.put(jug.getId(), jug.getColor().toLowerCase(Locale.ROOT));
        }
      }
    }

    return mapaColores;
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
}
