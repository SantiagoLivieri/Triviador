package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioRanking;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRanking {

  private final ServicioRanking servicioRanking;

  private static final String ATRIBUTO_USUARIO = "usuario";

  @Autowired
  public ControladorRanking(ServicioRanking servicioRanking) {
    this.servicioRanking = servicioRanking;
  }

  @GetMapping("/ranking")
  public ModelAndView mostrarRanking(HttpSession session) {
    final Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

    final ModelMap modelo = new ModelMap();

    final List<Usuario> top10 = servicioRanking.obtenerTop10General();
    final Long miPuesto = servicioRanking.calcularPuestoUsuario(usuarioLogueado);

    modelo.put("top10", top10);
    modelo.put("miPuesto", miPuesto);

    modelo.put(ATRIBUTO_USUARIO, usuarioLogueado);

    return new ModelAndView("ranking", modelo);
  }
}
