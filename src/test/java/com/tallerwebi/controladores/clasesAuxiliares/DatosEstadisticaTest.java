package com.tallerwebi.controladores.clasesAuxiliares;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tallerwebi.entidades.Comodin;
import org.junit.jupiter.api.Test;

public class DatosEstadisticaTest {

  @Test
  public void alCrearDatosEstadisticaDebeInicializarValoresEnCeroYNivelUno() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    assertEquals(0, datosEstadistica.getExperiencia());
    assertEquals(0, datosEstadistica.getMonedas());
    assertEquals(0, datosEstadistica.getComodinesEliminarDos());
    assertEquals(0, datosEstadistica.getComodinesDobleChance());
    assertEquals(0, datosEstadistica.getComodinesPasarPregunta());
    assertEquals(1, datosEstadistica.getNivelActual());
  }

  @Test
  public void alRegistrarFinDePartidaEnPrimerPuestoDebeSumarPartidaExperienciaMonedasYGanada() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.registrarFinDePartida(1, 200, 1000);

    assertEquals(1, datosEstadistica.getPartidasJugadas());
    assertEquals(1, datosEstadistica.getPartidasGanadas());
    assertEquals(200, datosEstadistica.getExperiencia());
    assertEquals(1000, datosEstadistica.getMonedas());
  }

  @Test
  public void alRegistrarFinDePartidaConValoresNullDebeTomarlosComoCero() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setPartidasJugadas(null);
    datosEstadistica.setPartidasGanadas(null);
    datosEstadistica.setExperiencia(null);
    datosEstadistica.setMonedas(null);

    datosEstadistica.registrarFinDePartida(1, 100, 50);

    assertEquals(1, datosEstadistica.getPartidasJugadas());
    assertEquals(1, datosEstadistica.getPartidasGanadas());
    assertEquals(100, datosEstadistica.getExperiencia());
    assertEquals(50, datosEstadistica.getMonedas());
  }

  @Test
  public void alRegistrarFinDePartidaConPerdidaNoDebeDejarExperienciaNiMonedasNegativas() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setExperiencia(50);
    datosEstadistica.setMonedas(5);
    datosEstadistica.setPartidasGanadas(0);

    datosEstadistica.registrarFinDePartida(3, -100, -10);

    assertEquals(1, datosEstadistica.getPartidasJugadas());
    assertEquals(0, datosEstadistica.getPartidasGanadas());
    assertEquals(0, datosEstadistica.getExperiencia());
    assertEquals(0, datosEstadistica.getMonedas());
  }

  @Test
  public void alComprarComodinEliminarDosDebeDescontarMonedasYSumarInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(100);

    Comodin comodin = new Comodin("ELIMINAR_2", "Elimina dos opciones", 50);

    datosEstadistica.registrarCompraDeItem(comodin);

    assertEquals(50, datosEstadistica.getMonedas());
    assertEquals(1, datosEstadistica.getComodinesEliminarDos());
  }

  @Test
  public void alComprarComodinDobleChanceDebeDescontarMonedasYSumarInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(100);

    Comodin comodin = new Comodin("DOBLE_CHANCE", "Permite responder dos veces", 35);

    datosEstadistica.registrarCompraDeItem(comodin);

    assertEquals(65, datosEstadistica.getMonedas());
    assertEquals(1, datosEstadistica.getComodinesDobleChance());
  }

  @Test
  public void alComprarComodinPasarPreguntaDebeDescontarMonedasYSumarInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(100);

    Comodin comodin = new Comodin("PASAR_PREGUNTA", "Permite pasar una pregunta", 25);

    datosEstadistica.registrarCompraDeItem(comodin);

    assertEquals(75, datosEstadistica.getMonedas());
    assertEquals(1, datosEstadistica.getComodinesPasarPregunta());
  }

  @Test
  public void alComprarComodinSinMonedasSuficientesDebeLanzarExcepcion() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(10);

    Comodin comodin = new Comodin("ELIMINAR_2", "Elimina dos opciones", 50);

    IllegalStateException excepcion = assertThrows(
      IllegalStateException.class,
      () -> datosEstadistica.registrarCompraDeItem(comodin)
    );

    assertEquals(
      "No tenes suficientes TriviaCoins para comprar este ítem.",
      excepcion.getMessage()
    );
  }

  @Test
  public void alComprarItemDesconocidoDebeLanzarExcepcion() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(100);

    Comodin comodin = new Comodin("ITEM_RARO", "Item raro", 10);

    IllegalArgumentException excepcion = assertThrows(
      IllegalArgumentException.class,
      () -> datosEstadistica.registrarCompraDeItem(comodin)
    );

    assertEquals("Item no reconocido.", excepcion.getMessage());
  }

  @Test
  public void alConsumirComodinEliminarDosDebeRestarUnoDelInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setComodinesEliminarDos(2);

    datosEstadistica.consumirComodin("ELIMINAR_2");

    assertEquals(1, datosEstadistica.getComodinesEliminarDos());
  }

  @Test
  public void alConsumirComodinDobleChanceDebeRestarUnoDelInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setComodinesDobleChance(2);

    datosEstadistica.consumirComodin("DOBLE_CHANCE");

    assertEquals(1, datosEstadistica.getComodinesDobleChance());
  }

  @Test
  public void alConsumirComodinPasarPreguntaDebeRestarUnoDelInventario() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setComodinesPasarPregunta(2);

    datosEstadistica.consumirComodin("PASAR_PREGUNTA");

    assertEquals(1, datosEstadistica.getComodinesPasarPregunta());
  }

  @Test
  public void alConsumirComodinSinInventarioDebeLanzarExcepcion() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setComodinesEliminarDos(0);

    IllegalStateException excepcion = assertThrows(
      IllegalStateException.class,
      () -> datosEstadistica.consumirComodin("ELIMINAR_2")
    );

    assertEquals("No tenés este comodín en tu inventario.", excepcion.getMessage());
  }

  @Test
  public void alConsumirComodinDesconocidoDebeLanzarExcepcion() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    IllegalArgumentException excepcion = assertThrows(
      IllegalArgumentException.class,
      () -> datosEstadistica.consumirComodin("COMODIN_RARO")
    );

    assertEquals("Item no reconocido.", excepcion.getMessage());
  }

  @Test
  public void alCalcularExperienciaSegunPuestoDebeRetornarValoresCorrectos() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    assertEquals(200, datosEstadistica.calcularXPSegunPuesto(1));
    assertEquals(100, datosEstadistica.calcularXPSegunPuesto(2));
    assertEquals(-100, datosEstadistica.calcularXPSegunPuesto(3));
  }

  @Test
  public void alCalcularMonedasSegunPuestoDebeRetornarValoresCorrectos() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    assertEquals(1000, datosEstadistica.calcularMonedasSegunPuesto(1));
    assertEquals(50, datosEstadistica.calcularMonedasSegunPuesto(2));
    assertEquals(10, datosEstadistica.calcularMonedasSegunPuesto(3));
  }

  @Test
  public void alCalcularNivelActualDebeRetornarNivelSegunExperiencia() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setExperiencia(null);
    assertEquals(1, datosEstadistica.getNivelActual());

    datosEstadistica.setExperiencia(199);
    assertEquals(1, datosEstadistica.getNivelActual());

    datosEstadistica.setExperiencia(200);
    assertEquals(2, datosEstadistica.getNivelActual());

    datosEstadistica.setExperiencia(450);
    assertEquals(3, datosEstadistica.getNivelActual());
  }

  @Test
  public void losGettersDeComodinesYMonedasDebenRetornarCeroSiElValorEsNull() {
    DatosEstadistica datosEstadistica = new DatosEstadistica();

    datosEstadistica.setMonedas(null);
    datosEstadistica.setComodinesEliminarDos(null);
    datosEstadistica.setComodinesDobleChance(null);
    datosEstadistica.setComodinesPasarPregunta(null);

    assertEquals(0, datosEstadistica.getMonedas());
    assertEquals(0, datosEstadistica.getComodinesEliminarDos());
    assertEquals(0, datosEstadistica.getComodinesDobleChance());
    assertEquals(0, datosEstadistica.getComodinesPasarPregunta());
  }
}
