package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.Usuario;
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

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.mostrarTienda(session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertSame(catalogo, modelo.get("comodines"));
    assertSame(usuarioActualizado, modelo.get("usuario"));

    verify(session).getAttribute("usuarioLogueado");
    verify(servicioUsuario).buscarUsuarioPorId(1L);
    verify(servicioTienda).obtenerCatalogoDeComodines();
  }

  @Test
  public void alComprarComodinCorrectamenteDebeMostrarMensajeDeExitoActualizarSesionYRetornarTienda() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    usuarioActualizado.setId(1L);

    String tipoComodin = "ELIMINAR_2";

    List<Comodin> catalogo = List.of(new Comodin("ELIMINAR_2", "Elimina dos opciones", 100));

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertEquals("¡Comodín adquirido con éxito!", modelo.get("exito"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(catalogo, modelo.get("comodines"));

    verify(servicioTienda).procesarCompraDeComodin(1L, tipoComodin);
    verify(servicioUsuario).buscarUsuarioPorId(1L);
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
    verify(servicioTienda).obtenerCatalogoDeComodines();
  }

  @Test
  public void alComprarComodinConIllegalArgumentExceptionDebeMostrarMensajeDeError() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    String tipoComodin = "ELIMINAR_2";

    List<Comodin> catalogo = List.of(new Comodin("ELIMINAR_2", "Elimina dos opciones", 100));

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);

    doThrow(new IllegalArgumentException("No tenes monedas suficientes."))
      .when(servicioTienda)
      .procesarCompraDeComodin(1L, tipoComodin);

    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertEquals("No tenes monedas suficientes.", modelo.get("error"));
    assertSame(usuarioActualizado, modelo.get("usuario"));
    assertSame(catalogo, modelo.get("comodines"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alComprarComodinConIllegalStateExceptionDebeMostrarMensajeDeError() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    usuarioLogueado.setId(1L);

    Usuario usuarioActualizado = new Usuario();
    String tipoComodin = "DOBLE_CHANCE";

    List<Comodin> catalogo = List.of(
      new Comodin("DOBLE_CHANCE", "Permite responder dos veces", 150)
    );

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);

    doThrow(new IllegalStateException("El comodín no se puede comprar ahora."))
      .when(servicioTienda)
      .procesarCompraDeComodin(1L, tipoComodin);

    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
    when(servicioTienda.obtenerCatalogoDeComodines()).thenReturn(catalogo);

    // Ejecución
    ModelAndView modelAndView = controladorTienda.comprarComodin(tipoComodin, session);

    // Verificación
    assertEquals("tienda", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertEquals("El comodín no se puede comprar ahora.", modelo.get("error"));
    assertTrue(modelo.containsKey("usuario"));
    assertTrue(modelo.containsKey("comodines"));

    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }
}
