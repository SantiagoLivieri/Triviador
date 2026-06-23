package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioTienda;
import com.tallerwebi.servicios.ServicioUsuario;
import java.util.List;
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

  private static final String VISTA_TIENDA = "tienda";
  private static final String ATRIBUTO_USUARIO = "usuario";

  @Autowired
  public ControladorTienda(ServicioTienda servicioTienda, ServicioUsuario servicioUsuario) {
    this.servicioTienda = servicioTienda;
    this.servicioUsuario = servicioUsuario;
  }

  @GetMapping("/tienda")
  public ModelAndView mostrarTienda(HttpSession session) {
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

    ModelMap modelo = new ModelMap();
    Usuario usuario = servicioUsuario.buscarUsuarioPorId(usuarioLogueado.getId());
    List<Comodin> catalogo = servicioTienda.obtenerCatalogoDeComodines();

    modelo.put("comodines", catalogo);
    modelo.put(ATRIBUTO_USUARIO, usuario);

    return new ModelAndView(VISTA_TIENDA, modelo);
  }

  @PostMapping("/tienda/comprar")
  public ModelAndView comprarComodin(
    @RequestParam("tipo") String tipoComodin,
    HttpSession session
  ) {
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    ModelMap modelo = new ModelMap();

    try {
      servicioTienda.procesarCompraDeComodin(usuarioLogueado.getId(), tipoComodin);
      modelo.put("exito", "¡Comodín adquirido con éxito!");
    } catch (IllegalArgumentException | IllegalStateException e) {
      modelo.put("error", e.getMessage());
    }

    Usuario usuarioActualizado = servicioUsuario.buscarUsuarioPorId(usuarioLogueado.getId());

    session.setAttribute("usuarioLogueado", usuarioActualizado);
    modelo.put(ATRIBUTO_USUARIO, usuarioActualizado);

    List<Comodin> catalogo = servicioTienda.obtenerCatalogoDeComodines();
    modelo.put("comodines", catalogo);

    return new ModelAndView(VISTA_TIENDA, modelo);
  }
}
