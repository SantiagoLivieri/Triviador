package com.tallerwebi.entidades;

import com.tallerwebi.controladores.clasesAuxiliares.DatosEstadistica;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private String email;

  private String password;
  private Integer triviaCoins;
  private Boolean activo;

  @Column(name = "nombre_jugador", length = 100)
  private String nombreJugador;

  @ManyToOne
  @JoinColumn(name = "rol_id")
  private Rol rol;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  private List<HistorialPartida> historial = new ArrayList<>();

  @Embedded
  private DatosEstadistica estadisticas = new DatosEstadistica();

  public Usuario() {}

  public Usuario(DatosRegistro datosRegistro, Rol rolUser) {
    this.nombre = datosRegistro.getNombre();
    this.email = datosRegistro.getEmail();
    this.password = datosRegistro.getPassword();
    this.activo = true;
    this.rol = rolUser;
    this.estadisticas = new DatosEstadistica();
  }

  public int registrarFinDePartida(Integer puesto) {
    if (this.estadisticas == null) {
      this.estadisticas = new DatosEstadistica();
    }
    int xpGanada = estadisticas.calcularXPSegunPuesto(puesto);
    int monedasGanadas = estadisticas.calcularMonedasSegunPuesto(puesto);

    this.estadisticas.registrarFinDePartida(puesto, xpGanada, monedasGanadas);
    return xpGanada;
  }

  public void adquirirComodin(Comodin comodin) {
    if (this.estadisticas == null) {
      this.estadisticas = new DatosEstadistica();
    }
    this.estadisticas.registrarCompraDeItem(comodin);
  }

  public void consumirComodin(String comodin) {
    if (this.estadisticas == null) {
      this.estadisticas = new DatosEstadistica();
    }
    this.estadisticas.consumirComodin(comodin);
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

  public Integer getExperiencia() {
    return estadisticas != null ? estadisticas.getExperiencia() : 0;
  }

  public Integer getPartidasJugadas() {
    return estadisticas != null ? estadisticas.getPartidasJugadas() : 0;
  }

  public Integer getPartidasGanadas() {
    return estadisticas != null ? estadisticas.getPartidasGanadas() : 0;
  }

  public int getNivel() {
    return estadisticas != null ? estadisticas.getNivelActual() : 1;
  }

  public Integer getMonedas() {
    return estadisticas != null ? estadisticas.getMonedas() : 0;
  }

  public Integer getComodinesEliminarDos() {
    return estadisticas != null ? estadisticas.getComodinesEliminarDos() : 0;
  }

  public Integer getComodinesDobleChance() {
    return estadisticas != null ? estadisticas.getComodinesDobleChance() : 0;
  }

  public Integer getComodinesPasarPregunta() {
    return estadisticas != null ? estadisticas.getComodinesPasarPregunta() : 0;
  }

  public DatosEstadistica getEstadisticas() {
    return estadisticas;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public Integer getTriviaCoins() {
    return triviaCoins;
  }

  public void setTriviaCoins(Integer triviaCoins) {
    this.triviaCoins = triviaCoins;
  }
}
