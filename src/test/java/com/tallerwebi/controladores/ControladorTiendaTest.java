package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.PaqueteMonedas;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioPago;
import com.tallerwebi.servicios.ServicioTienda;
import com.tallerwebi.servicios.ServicioUsuario;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
public class ControladorTiendaTest {

  /*
   * Mockito:
   *
   * Mockeamos ServicioTienda porque el controlador no debería probar
   * la lógica de compra, solo debe delegarla al servicio.
   */
  @Mock
  private ServicioTienda servicioTienda;

  /*
   * Mockito:
   *
   * Mockeamos ServicioPago porque el controlador solo debe delegar
   * la creación de la preferencia y recibir la URL, sin conectarse a MP.
   */
  @Mock
  private ServicioPago servicioPago;

  /*
   * Mockito:
   *
   * Mockeamos ServicioUsuario porque el controlador necesita buscar
   * el usuario actualizado, pero no queremos tocar base de datos.
   */
  @Mock
  private ServicioUsuario servicioUsuario;

  /*
   * Mockito:
   *
   * Mockeamos HttpSession para simular que hay un usuario logueado.
   */
  @Mock
  private HttpSession session;

  /*
   * Mockito:
   *
   * @InjectMocks crea el controlador real e inyecta los servicios mockeados.
   */
  @InjectMocks
  private ControladorTienda controladorTienda;

  @Test
  public void alMostrarTiendaDebeRetornarVistaTiendaConUsuarioYCatalogo() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    Comodin comodinUno = new Comodin("ELIMINAR_2", "Elimina dos opciones", 100);
    Comodin comodinDos = new Comodin("DOBLE_CHANCE", "Permite responder dos veces", 150);

    List<Comodin> catalogo = List.of(comodinUno, comodinDos);

    PaqueteMonedas paquetePrueba = new PaqueteMonedas();
    List<PaqueteMonedas> paquetes = List.of(paquetePrueba);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);
    when(servicioTienda.obtenerTodosLosPaquetes()).thenReturn(paquetes);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.mostrarTienda(session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertSame(catalogo, modelo.get("comodines"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(paquetes, modelo.get("paquetes"));

    verify(session).getAttribute("usuarioLogueado");
    verify(servicioUsuario).buscarUsuarioPorId(1L);
    verify(servicioTienda).obtenerCatalogoDeComodines();
    verify(servicioTienda).obtenerTodosLosPaquetes();
  }

  @Test
  public void alComprarComodinCorrectamenteDebeMostrarMensajeDeExitoActualizarSesionYRetornarTienda() {
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    usuarioActualizado.setId(1L);

    String tipoComodin = "ELIMINAR_2";
    List<Comodin> catalogo = List.of(new Comodin("ELIMINAR_2", "Elimina dos opciones", 100));

    PaqueteMonedas paquetePrueba = new PaqueteMonedas();
    List<PaqueteMonedas> paquetes = List.of(paquetePrueba);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);

    when(servicioTienda.obtenerTodosLosPaquetes()).thenReturn(paquetes);

    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();
    assertEquals("¡Comodín adquirido con éxito!", modelo.get("exito"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(catalogo, modelo.get("comodines"));

    assertSame(paquetes, modelo.get("paquetes"));

    verify(servicioTienda).procesarCompraDeComodin(1L, tipoComodin);
    verify(servicioUsuario).buscarUsuarioPorId(1L);
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
    verify(servicioTienda).obtenerCatalogoDeComodines();

    verify(servicioTienda).obtenerTodosLosPaquetes();
  }

  @Test
  public void alComprarComodinConIllegalArgumentExceptionDebeMostrarMensajeDeError() {
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    String tipoComodin = "ELIMINAR_2";

    List<Comodin> catalogo = List.of(new Comodin("ELIMINAR_2", "Elimina dos opciones", 100));
    List<PaqueteMonedas> paquetes = List.of(new PaqueteMonedas());

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);

    doThrow(new IllegalArgumentException("No tenes monedas suficientes."))
      .when(servicioTienda)
      .procesarCompraDeComodin(1L, tipoComodin);

    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);
    when(servicioTienda.obtenerTodosLosPaquetes()).thenReturn(paquetes);

    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertEquals("No tenes monedas suficientes.", modelo.get("error"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(catalogo, modelo.get("comodines"));
    assertSame(paquetes, modelo.get("paquetes"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
    verify(servicioTienda).obtenerTodosLosPaquetes();
  }

  @Test
  public void alComprarComodinConIllegalStateExceptionDebeMostrarMensajeDeError() {
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    String tipoComodin = "DOBLE_CHANCE";

    List<Comodin> catalogo = List.of(
      new Comodin("DOBLE_CHANCE", "Permite responder dos veces", 150)
    );
    List<PaqueteMonedas> paquetes = List.of(new PaqueteMonedas());

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);

    doThrow(new IllegalStateException("El comodín no se puede comprar ahora."))
      .when(servicioTienda)
      .procesarCompraDeComodin(1L, tipoComodin);

    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);
    when(servicioTienda.obtenerTodosLosPaquetes()).thenReturn(paquetes);

    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertEquals("El comodín no se puede comprar ahora.", modelo.get("error"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(catalogo, modelo.get("comodines"));
    assertSame(paquetes, modelo.get("paquetes"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
    verify(servicioTienda).obtenerTodosLosPaquetes();
  }

  @Test
  public void alIniciarCompraCoinsDebeRedirigirAUrlDePago() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    PaqueteMonedas paquete = new PaqueteMonedas();
    paquete.setId(10L);

    String urlEsperada = "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=123";

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioTienda.buscarPaquetePorId(10L)).thenReturn(paquete);
    when(servicioPago.crearPreferenciaDePago(paquete, 1L)).thenReturn(urlEsperada);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.iniciarCompraCoins(10L, session);

    // Verificación
    assertEquals("redirect:" + urlEsperada, modelAndView.getViewName());

    verify(session).getAttribute("usuarioLogueado");
    verify(servicioTienda).buscarPaquetePorId(10L);
    verify(servicioPago).crearPreferenciaDePago(paquete, 1L);
  }

  @Test
  public void alRetornarPagoExitosoDebeMostrarMensajeDeExitoYCargarModeloBase() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);
    Usuario usuarioActualizado = new Usuario();

    List<Comodin> comodines = List.of(new Comodin());
    List<PaqueteMonedas> paquetes = List.of(new PaqueteMonedas());

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(comodines);
    when(servicioTienda.obtenerTodosLosPaquetes()).thenReturn(paquetes);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.pagoExitoso(session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();
    assertEquals("¡Pago aprobado! Tus TriviaCoins ya están listas.", modelo.get("exito"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(comodines, modelo.get("comodines"));
    assertSame(paquetes, modelo.get("paquetes"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alRetornarPagoPendienteDebeMostrarMensajePendienteYCargarModeloBase() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);
    Usuario usuarioActualizado = new Usuario();

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    // No hace falta mockear las listas completas si solo verificamos que no
    // devuelva null
    // pero Mockito devolverá colecciones vacías por defecto si no lo hacemos.

    // Ejecución
    ModelAndView modelAndView = controladorTienda.pagoPendiente(session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();
    assertEquals(
      "Tu pago está pendiente. Tus TriviaCoins se sumarán cuando MP confirme la operación.",
      modelo.get("exito")
    );
    assertSame(usuarioActualizado, modelo.get("usuario"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alRetornarPagoFallidoDebeMostrarMensajeDeErrorYCargarModeloBase() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);
    Usuario usuarioActualizado = new Usuario();

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.pagoFallido(session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();
    assertEquals(
      "El pago fue cancelado o rechazado. No se han realizado cargos.",
      modelo.get("error")
    );
    assertSame(usuarioActualizado, modelo.get("usuario"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }
}
