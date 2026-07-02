package com.tallerwebi.controladores;

import com.tallerwebi.entidades.PaqueteMonedas;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioPago;
import com.tallerwebi.servicios.ServicioTienda;
import com.tallerwebi.servicios.ServicioUsuario;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorTienda {

  private final ServicioTienda servicioTienda;
  private final ServicioUsuario servicioUsuario;
  private final ServicioPago servicioPago;

  private static final String VISTA_TIENDA = "tienda";
  private static final String ATTR_USUARIO = "usuario";
  private static final String ATTR_USUARIO_LOGUEADO = "usuarioLogueado";

  @Autowired
  public ControladorTienda(
    ServicioTienda servicioTienda,
    ServicioUsuario servicioUsuario,
    ServicioPago servicioPago
  ) {
    this.servicioTienda = servicioTienda;
    this.servicioUsuario = servicioUsuario;
    this.servicioPago = servicioPago;
  }

  @GetMapping("/tienda")
  public ModelAndView mostrarTienda(HttpSession session) {
    ModelMap modelo = cargarModeloBaseTienda(session);

    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  @PostMapping("/tienda/comprar")
  public ModelAndView comprarComodin(
    @RequestParam("tipo") String tipoComodin,
    HttpSession session
  ) {
    Usuario usuarioLogueado = (Usuario) session.getAttribute(ATTR_USUARIO_LOGUEADO);
    ModelMap modelo = new ModelMap();

    try {
      servicioTienda.procesarCompraDeComodin(usuarioLogueado.getId(), tipoComodin);
      modelo.put("exito", "¡Comodín adquirido con éxito!");
    } catch (IllegalArgumentException | IllegalStateException e) {
      modelo.put("error", e.getMessage());
    }

    Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuarioLogueado.getId());
    session.setAttribute(ATTR_USUARIO_LOGUEADO, usuarioActualizado);

    modelo.put(ATTR_USUARIO, usuarioActualizado);
    modelo.put("comodines", servicioTienda.obtenerCatalogoDeComodines());
    modelo.put("paquetes", servicioTienda.obtenerTodosLosPaquetes());

    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  @PostMapping("/tienda/comprar-coins")
  public ModelAndView iniciarCompraCoins(
    @RequestParam("idPaquete") Long idPaquete,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(ATTR_USUARIO_LOGUEADO);
    PaqueteMonedas paquete = servicioTienda.buscarPaquetePorId(idPaquete);

    String urlDePago = servicioPago.crearPreferenciaDePago(paquete, usuario.getId());

    return new ModelAndView("redirect:" + urlDePago);
  }

  @GetMapping("/tienda/exito")
  public ModelAndView pagoExitoso(HttpSession session) {
    ModelMap modelo = cargarModeloBaseTienda(session);

    modelo.put("exito", "¡Pago aprobado! Tus TriviaCoins ya están listas.");
    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  @GetMapping("/tienda/pendiente")
  public ModelAndView pagoPendiente(HttpSession session) {
    ModelMap modelo = cargarModeloBaseTienda(session);
    modelo.put(
      "exito",
      "Tu pago está pendiente. Tus TriviaCoins se sumarán cuando MP confirme la operación."
    );
    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  @GetMapping("/tienda/error")
  public ModelAndView pagoFallido(HttpSession session) {
    ModelMap modelo = cargarModeloBaseTienda(session);
    modelo.put("error", "El pago fue cancelado o rechazado. No se han realizado cargos.");
    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  private ModelMap cargarModeloBaseTienda(HttpSession session) {
    Usuario usuarioLogueado = (Usuario) session.getAttribute(ATTR_USUARIO_LOGUEADO);
    Usuario usuario = servicioUsuario.buscarUsuarioPorId(usuarioLogueado.getId());

    session.setAttribute(ATTR_USUARIO_LOGUEADO, usuario);

    ModelMap modelo = new ModelMap();
    modelo.put("comodines", servicioTienda.obtenerCatalogoDeComodines());
    modelo.put("paquetes", servicioTienda.obtenerTodosLosPaquetes());
    modelo.put(ATTR_USUARIO, usuario);

    return modelo;
  }
}
