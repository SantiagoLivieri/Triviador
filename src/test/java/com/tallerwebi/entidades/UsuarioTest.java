package com.tallerwebi.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosEstadistica;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

  @Test
  public void alCrearUsuarioConConstructorVacioDebePermitirSetearYObtenerDatosBasicos() {
    // Arrange:
    // Creamos un usuario vacío y un rol real.
    // Este test valida los setters y getters básicos de la entidad.
    Usuario usuario = new Usuario();
    Rol rol = new Rol();

    // Act:
    // Cargamos manualmente los datos del usuario.
    usuario.setId(1L);
    usuario.setNombre("Santiago");
    usuario.setEmail("santi@test.com");
    usuario.setPassword("1234");
    usuario.setActivo(false);
    usuario.setNombreJugador("Santi");
    usuario.setRol(rol);

    // Assert:
    // Verificamos que cada getter devuelva exactamente lo que seteamos.
    assertEquals(1L, usuario.getId());
    assertEquals("Santiago", usuario.getNombre());
    assertEquals("santi@test.com", usuario.getEmail());
    assertEquals("1234", usuario.getPassword());
    assertEquals(false, usuario.getActivo());
    assertEquals("Santi", usuario.getNombreJugador());

    // assertSame valida que sea la misma instancia de Rol, no solo un objeto parecido.
    assertSame(rol, usuario.getRol());
  }

  @Test
  public void alCrearUsuarioDesdeDatosRegistroDebeCopiarDatosActivarloYAsignarRol() {
    // Arrange:
    // Mockeamos DatosRegistro porque Usuario recibe ese objeto en el constructor.
    // No nos interesa probar DatosRegistro acá, solo cómo Usuario lo usa.
    DatosRegistro datosRegistro = mock(DatosRegistro.class);

    // Usamos un Rol real porque solo queremos verificar que quede asignado.
    Rol rol = new Rol();

    // Configuramos qué debe devolver el mock cuando el constructor le pida datos.
    when(datosRegistro.getNombre()).thenReturn("Santiago");
    when(datosRegistro.getEmail()).thenReturn("santi@test.com");
    when(datosRegistro.getPassword()).thenReturn("1234");

    // Act:
    // Ejecutamos el constructor que recibe DatosRegistro y Rol.
    Usuario usuario = new Usuario(datosRegistro, rol);

    // Assert:
    // Validamos que el constructor copie correctamente los datos.
    assertEquals("Santiago", usuario.getNombre());
    assertEquals("santi@test.com", usuario.getEmail());
    assertEquals("1234", usuario.getPassword());

    // También validamos las reglas internas del constructor:
    // el usuario debe quedar activo, con rol asignado y estadísticas inicializadas.
    assertTrue(usuario.getActivo());
    assertSame(rol, usuario.getRol());
    assertEquals(0, usuario.getExperiencia());
    assertEquals(0, usuario.getMonedas());
  }

  @Test
  public void alActivarUsuarioDebeCambiarActivoATrue() {
    // Arrange:
    // Creamos un usuario y lo dejamos inactivo.
    Usuario usuario = new Usuario();

    usuario.setActivo(false);

    // Act:
    // Ejecutamos el método que queremos probar.
    usuario.activar();

    // Assert:
    // Validamos que activar() cambie el estado a true.
    assertTrue(usuario.getActivo());
  }

  @Test
  public void alRegistrarFinDePartidaEnPrimerPuestoDebeSumarEstadisticasYRetornarExperienciaGanada() {
    // Arrange:
    // Usuario arranca con estadísticas en cero.
    Usuario usuario = new Usuario();

    // Act:
    // Registramos fin de partida en primer puesto.
    int xpGanada = usuario.registrarFinDePartida(1);

    // Assert:
    // Para primer puesto, DatosEstadistica otorga 200 XP y 1000 monedas.
    assertEquals(200, xpGanada);
    assertEquals(200, usuario.getExperiencia());
    assertEquals(1000, usuario.getMonedas());

    // También debe sumar una partida jugada y una ganada.
    assertEquals(1, usuario.getPartidasJugadas());
    assertEquals(1, usuario.getPartidasGanadas());

    // Con 200 XP, el nivel pasa a 2.
    assertEquals(2, usuario.getNivel());
  }

  @Test
  public void alRegistrarFinDePartidaEnSegundoPuestoDebeSumarExperienciaYMonedasSinGanarPartida() {
    // Arrange:
    // Creamos un usuario nuevo.
    Usuario usuario = new Usuario();

    // Act:
    // Registramos fin de partida en segundo puesto.
    int xpGanada = usuario.registrarFinDePartida(2);

    // Assert:
    // Segundo puesto otorga 100 XP y 50 monedas.
    assertEquals(100, xpGanada);
    assertEquals(100, usuario.getExperiencia());
    assertEquals(50, usuario.getMonedas());

    // Debe sumar partida jugada, pero no partida ganada.
    assertEquals(1, usuario.getPartidasJugadas());
    assertEquals(0, usuario.getPartidasGanadas());

    // Con 100 XP sigue en nivel 1.
    assertEquals(1, usuario.getNivel());
  }

  @Test
  public void alRegistrarFinDePartidaSiEstadisticasEsNullDebeInicializarlas() throws Exception {
    // Arrange:
    // Creamos un usuario y forzamos sus estadísticas a null.
    // Esto permite probar la rama defensiva del método registrarFinDePartida.
    Usuario usuario = new Usuario();

    setEstadisticas(usuario, null);

    // Act:
    // Al registrar una partida, Usuario debería crear un nuevo DatosEstadistica.
    int xpGanada = usuario.registrarFinDePartida(1);

    // Assert:
    // Validamos que no haya fallado por NullPointerException
    // y que las estadísticas hayan quedado correctamente actualizadas.
    assertEquals(200, xpGanada);
    assertEquals(200, usuario.getExperiencia());
    assertEquals(1000, usuario.getMonedas());
  }

  @Test
  public void alAdquirirComodinDebeDescontarMonedasYSumarComodinAlInventario() {
    // Arrange:
    // Creamos un usuario y le damos monedas registrando una victoria.
    Usuario usuario = new Usuario();

    usuario.registrarFinDePartida(1);

    // El comodín cuesta 50 monedas.
    Comodin comodin = new Comodin("ELIMINAR_2", "Elimina dos opciones", 50);

    // Act:
    // Compramos el comodín.
    usuario.adquirirComodin(comodin);

    // Assert:
    // Tenía 1000 monedas, gasta 50, quedan 950.
    assertEquals(950, usuario.getMonedas());

    // Además debe sumarse un comodín ELIMINAR_2 al inventario.
    assertEquals(1, usuario.getComodinesEliminarDos());
  }

  @Test
  public void alAdquirirComodinSiEstadisticasEsNullDebeInicializarlasYLanzarErrorSiNoTieneMonedas()
    throws Exception {
    // Arrange:
    // Creamos un usuario y forzamos estadísticas a null.
    Usuario usuario = new Usuario();

    setEstadisticas(usuario, null);

    // Este comodín cuesta 25 monedas.
    // Como las estadísticas se inicializan desde cero, el usuario tendrá 0 monedas.
    Comodin comodin = new Comodin("PASAR_PREGUNTA", "Permite pasar pregunta", 25);

    // Act + Assert:
    // La compra debe fallar porque el usuario no tiene monedas suficientes.
    IllegalStateException excepcion = assertThrows(
      IllegalStateException.class,
      () -> usuario.adquirirComodin(comodin)
    );

    // Validamos el mensaje exacto de la excepción.
    assertEquals(
      "No tenes suficientes TriviaCoins para comprar este ítem.",
      excepcion.getMessage()
    );

    // También confirmamos que, aunque la compra falló,
    // Usuario sí inicializó sus estadísticas.
    assertEquals(0, usuario.getMonedas());
    assertEquals(0, usuario.getComodinesPasarPregunta());
  }

  @Test
  public void alConsumirComodinDebeRestarUnoDelInventario() {
    // Arrange:
    // Creamos un usuario y le damos monedas.
    Usuario usuario = new Usuario();

    usuario.registrarFinDePartida(1);

    // Compramos un comodín DOBLE_CHANCE.
    Comodin comodin = new Comodin("DOBLE_CHANCE", "Doble chance", 35);

    usuario.adquirirComodin(comodin);

    // Act:
    // Consumimos el comodín recién comprado.
    usuario.consumirComodin("DOBLE_CHANCE");

    // Assert:
    // Como tenía 1, después de consumirlo debe quedar en 0.
    assertEquals(0, usuario.getComodinesDobleChance());
  }

  @Test
  public void alConsumirComodinSiEstadisticasEsNullDebeInicializarlasYLanzarErrorSiNoTieneInventario()
    throws Exception {
    // Arrange:
    // Creamos un usuario y forzamos estadísticas en null.
    Usuario usuario = new Usuario();

    setEstadisticas(usuario, null);

    // Act + Assert:
    // Al consumir un comodín sin inventario, debe lanzar IllegalStateException.
    IllegalStateException excepcion = assertThrows(
      IllegalStateException.class,
      () -> usuario.consumirComodin("ELIMINAR_2")
    );

    // Validamos el mensaje exacto.
    assertEquals("No tenés este comodín en tu inventario.", excepcion.getMessage());

    // También validamos que Usuario haya inicializado estadísticas.
    assertEquals(0, usuario.getComodinesEliminarDos());
  }

  @Test
  public void losGettersDeEstadisticasDebenRetornarValoresPorDefectoSiEstadisticasEsNull()
    throws Exception {
    // Arrange:
    // Forzamos estadísticas a null para validar que los getters sean seguros.
    Usuario usuario = new Usuario();

    setEstadisticas(usuario, null);

    // Assert:
    // Ningún getter debería tirar NullPointerException.
    // Todos deben devolver valores por defecto.
    assertEquals(0, usuario.getExperiencia());
    assertEquals(0, usuario.getPartidasJugadas());
    assertEquals(0, usuario.getPartidasGanadas());
    assertEquals(1, usuario.getNivel());
    assertEquals(0, usuario.getMonedas());
    assertEquals(0, usuario.getComodinesEliminarDos());
    assertEquals(0, usuario.getComodinesDobleChance());
    assertEquals(0, usuario.getComodinesPasarPregunta());
  }

  @Test
  public void alObtenerEstadisticasDebeRetornarLaInstanciaActual() {
    // Arrange:
    // Creamos un usuario con estadísticas inicializadas por defecto.
    Usuario usuario = new Usuario();

    // Act:
    // Obtenemos la instancia de estadísticas.
    DatosEstadistica estadisticas = usuario.getEstadisticas();

    // Assert:
    // assertSame valida que getEstadisticas retorne la misma instancia interna.
    assertSame(estadisticas, usuario.getEstadisticas());
  }

  private void setEstadisticas(Usuario usuario, DatosEstadistica estadisticas) throws Exception {
    // Helper:
    // Usuario no tiene setter para estadisticas.
    // Como necesitamos probar ramas donde estadisticas es null,
    // usamos reflection para modificar ese atributo privado solo dentro del test.
    Field field = Usuario.class.getDeclaredField("estadisticas");

    // Permitimos acceder al atributo privado.
    field.setAccessible(true);

    // Seteamos el valor recibido: puede ser null o una instancia de DatosEstadistica.
    field.set(usuario, estadisticas);
  }
}
