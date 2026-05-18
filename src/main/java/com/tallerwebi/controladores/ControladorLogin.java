package com.tallerwebi.controladores;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.*;
import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLogin {

  private ServicioLogin servicioLogin;

  @Autowired
  public ControladorLogin(ServicioLogin servicioLogin) {
    this.servicioLogin = servicioLogin;
  }

  @RequestMapping("/login")
  public ModelAndView login() {
    ModelMap modelMap = new ModelMap();
    modelMap.put("datosLogin", new DatosLogin());
    return new ModelAndView("login", modelMap);
  }

  @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
  public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin) {
    ModelMap modelMap = new ModelMap();
    try {
      Usuario usuario = servicioLogin.validarDatos(datosLogin);
      modelMap.put("id", usuario.getId());
    } catch (UsuarioInexistenteException e) {
      modelMap.put("error", e.getMessage());
      return new ModelAndView("login", modelMap);
    }
    return new ModelAndView("redirect:/home");
  }

  @RequestMapping("/home")
  public ModelAndView home() {
    return new ModelAndView("home");
  }

  @RequestMapping("/registro")
  public ModelAndView registrar() {
    ModelMap modelMap = new ModelMap();
    modelMap.put("datosRegistro", new DatosRegistro());
    return new ModelAndView("registro", modelMap);
  }

  @RequestMapping(path = "/validarRegistro", method = RequestMethod.POST)
  public ModelAndView validarRegistro(
    @ModelAttribute("datosRegistro") DatosRegistro datosRegistro
  ) {
    ModelMap modelMap = new ModelMap();
    try {
      servicioLogin.validarEmail(datosRegistro.getEmail());
      servicioLogin.validarPassword(datosRegistro.getPassword(), datosRegistro.getRePassword());
      servicioLogin.crearUsuario(datosRegistro);
    } catch (UsuarioExistenteException | PasswordsDiferentesException e) {
      modelMap.put("error", e.getMessage());
      return new ModelAndView("registro", modelMap);
    }
    return new ModelAndView("redirect:/login");
  }
}
