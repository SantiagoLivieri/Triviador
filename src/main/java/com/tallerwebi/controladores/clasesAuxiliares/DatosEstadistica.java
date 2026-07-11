package com.tallerwebi.controladores.clasesAuxiliares;

import com.tallerwebi.entidades.Comodin;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DatosEstadistica {

  public static final int PUESTO_PRIMERO = 1;
  public static final int PUESTO_SEGUNDO = 2;
  public static final int XP_PRIMER_PUESTO = 200;
  public static final int XP_SEGUNDO_PUESTO = 100;
  public static final int XP_TERCER_PUESTO = -100;
  public static final int MONEDAS_PRIMER_PUESTO = 1000;
  public static final int MONEDAS_SEGUNDO_PUESTO = 50;
  public static final int MONEDAS_TERCER_PUESTO = 10;
  public static final int COSTO_ELIMINAR = 50;
  public static final int COSTO_DOBLE = 35;
  public static final int COSTO_PASAR = 25;

  @Column(nullable = false)
  private Integer experiencia = 0;

  @Column(name = "partidas_jugadas", nullable = false)
  private Integer partidasJugadas = 0;

  @Column(name = "partidas_ganadas", nullable = false)
  private Integer partidasGanadas = 0;

  @Column(name = "monedas", columnDefinition = "integer default 0")
  private Integer monedas = 0;

  @Column(name = "comodines_eliminar_dos")
  private Integer comodinesEliminarDos = 0;

  @Column(name = "comodines_doble_chance")
  private Integer comodinesDobleChance = 0;

  @Column(name = "comodines_pasar_pregunta")
  private Integer comodinesPasarPregunta = 0;

  public DatosEstadistica() {}

  public void registrarFinDePartida(Integer puesto, Integer xpGanada, Integer monedasGanadas) {
    this.partidasJugadas = (this.partidasJugadas == null) ? 1 : this.partidasJugadas + 1;

    int baseXp = (this.experiencia == null) ? 0 : this.experiencia;
    this.experiencia = Math.max(0, baseXp + xpGanada);

    int baseMonedas = (this.monedas == null) ? 0 : this.monedas;
    this.monedas = Math.max(0, baseMonedas + monedasGanadas);

    if (puesto == PUESTO_PRIMERO) {
      this.partidasGanadas = (this.partidasGanadas == null) ? 1 : this.partidasGanadas + 1;
    }
  }

  public void registrarCompraDeItem(Comodin comodin) {
    int saldoActual = obtenerValorSeguro(this.monedas);

    if (saldoActual < comodin.getCosto()) {
      throw new IllegalStateException("No tenes suficientes TriviaCoins para comprar este ítem.");
    }

    switch (comodin.getNombre()) {
      case "ELIMINAR_2":
        this.comodinesEliminarDos = obtenerValorSeguro(this.comodinesEliminarDos) + 1;
        break;
      case "DOBLE_CHANCE":
        this.comodinesDobleChance = obtenerValorSeguro(this.comodinesDobleChance) + 1;
        break;
      case "PASAR_PREGUNTA":
        this.comodinesPasarPregunta = obtenerValorSeguro(this.comodinesPasarPregunta) + 1;
        break;
      default:
        throw new IllegalArgumentException("Item no reconocido.");
    }

    this.monedas = saldoActual - comodin.getCosto();
  }

  public void consumirComodin(String comodin) {
    switch (comodin) {
      case "ELIMINAR_2":
        this.comodinesEliminarDos = decrementarInventario(this.comodinesEliminarDos);
        break;
      case "DOBLE_CHANCE":
        this.comodinesDobleChance = decrementarInventario(this.comodinesDobleChance);
        break;
      case "PASAR_PREGUNTA":
        this.comodinesPasarPregunta = decrementarInventario(this.comodinesPasarPregunta);
        break;
      default:
        throw new IllegalArgumentException("Item no reconocido.");
    }
  }

  private int obtenerValorSeguro(Integer valor) {
    return (valor == null) ? 0 : valor;
  }

  private Integer decrementarInventario(Integer cantidadActual) {
    if (cantidadActual == null || cantidadActual <= 0) throw new IllegalStateException(
      "No tenés este comodín en tu inventario."
    );

    return cantidadActual - 1;
  }

  public int calcularXPSegunPuesto(int puesto) {
    if (puesto == PUESTO_PRIMERO) return XP_PRIMER_PUESTO;

    if (puesto == PUESTO_SEGUNDO) return XP_SEGUNDO_PUESTO;

    return XP_TERCER_PUESTO;
  }

  public int calcularMonedasSegunPuesto(Integer puesto) {
    if (puesto == PUESTO_PRIMERO) return MONEDAS_PRIMER_PUESTO;

    if (puesto == PUESTO_SEGUNDO) return MONEDAS_SEGUNDO_PUESTO;

    return MONEDAS_TERCER_PUESTO;
  }

  public int getNivelActual() {
    if (this.experiencia == null || this.experiencia < 200) return 1;

    return (this.experiencia / 200) + 1;
  }

  public void sumarMonedas(Integer cantidad) {
    if (this.monedas == null) {
      this.monedas = 0;
    }
    this.monedas += cantidad;
  }

  public Integer getExperiencia() {
    if (this.experiencia == null) return 0;
    return this.experiencia;
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

  public Integer getMonedas() {
    return (this.monedas == null) ? 0 : this.monedas;
  }

  public void setMonedas(Integer monedas) {
    this.monedas = monedas;
  }

  public Integer getComodinesEliminarDos() {
    return comodinesEliminarDos == null ? 0 : comodinesEliminarDos;
  }

  public void setComodinesEliminarDos(Integer cantidad) {
    this.comodinesEliminarDos = cantidad;
  }

  public Integer getComodinesDobleChance() {
    return comodinesDobleChance == null ? 0 : comodinesDobleChance;
  }

  public void setComodinesDobleChance(Integer cantidad) {
    this.comodinesDobleChance = cantidad;
  }

  public Integer getComodinesPasarPregunta() {
    return comodinesPasarPregunta == null ? 0 : comodinesPasarPregunta;
  }

  public void setComodinesPasarPregunta(Integer cantidad) {
    this.comodinesPasarPregunta = cantidad;
  }

  public void restarExperiencia(Integer cantidad) {
    if (cantidad == null || cantidad < 0) {
      throw new IllegalArgumentException("La cantidad de experiencia a descontar debe ser válida.");
    }

    int experienciaActual = this.experiencia == null ? 0 : this.experiencia;

    this.experiencia = Math.max(0, experienciaActual - cantidad);
  }
}
