package com.tallerwebi.controladores;

import com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.ServicioSugerenciaPregunta;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorSugerenciaPregunta {

  private static final String USUARIO_LOGUEADO = "usuarioLogueado";
  private static final String DATOS_SUGERENCIA = "datosSugerenciaPregunta";
  private static final String VISTA_SUGERIR_PREGUNTA = "sugerir-pregunta";
  private static final String VISTA_ADMIN_SUGERENCIAS = "admin-sugerencias";
  private static final String VISTA_ADMIN_EDITAR_SUGERENCIA = "admin-editar-sugerencia";
  private static final String VISTA_ADMIN_CREAR_PREGUNTA = "admin-crear-pregunta";
  private static final String REDIRECT_HOME = "redirect:/home";
  private static final String REDIRECT_ADMIN_SUGERENCIAS = "redirect:/admin/sugerencias";
  private static final String ERROR = "error";
  private static final String EXITO = "exito";

  private final ServicioSugerenciaPregunta servicioSugerenciaPregunta;
  private final ServicioProvincia servicioProvincia;

  @Autowired
  public ControladorSugerenciaPregunta(
    ServicioSugerenciaPregunta servicioSugerenciaPregunta,
    ServicioProvincia servicioProvincia
  ) {
    this.servicioSugerenciaPregunta = servicioSugerenciaPregunta;
    this.servicioProvincia = servicioProvincia;
  }

  @GetMapping("/sugerir-pregunta")
  public ModelAndView mostrarFormularioSugerencia(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (!esJugador(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();
    cargarDatosFormulario(modelo);
    modelo.put(DATOS_SUGERENCIA, new DatosSugerenciaPregunta());

    return new ModelAndView(VISTA_SUGERIR_PREGUNTA, modelo);
  }

  @PostMapping("/sugerir-pregunta")
  public ModelAndView guardarSugerencia(
    @ModelAttribute(DATOS_SUGERENCIA) DatosSugerenciaPregunta datos,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    ModelMap modelo = new ModelMap();

    try {
      servicioSugerenciaPregunta.crearSugerencia(datos, usuario);
      modelo.put(EXITO, "¡Tu sugerencia fue enviada y quedó pendiente de aprobación!");
      modelo.put(DATOS_SUGERENCIA, new DatosSugerenciaPregunta());
    } catch (IllegalArgumentException e) {
      modelo.put(ERROR, e.getMessage());
      modelo.put(DATOS_SUGERENCIA, datos);
    }

    cargarDatosFormulario(modelo);

    return new ModelAndView(VISTA_SUGERIR_PREGUNTA, modelo);
  }

  @GetMapping("/admin/sugerencias")
  public ModelAndView mostrarSugerenciasAdmin(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (!esAdmin(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();
    modelo.put("sugerencias", servicioSugerenciaPregunta.obtenerSugerenciasPendientes());

    return new ModelAndView(VISTA_ADMIN_SUGERENCIAS, modelo);
  }

  @PostMapping("/admin/sugerencias/aprobar")
  public ModelAndView aprobarSugerencia(
    @RequestParam("id") Long idSugerencia,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    try {
      servicioSugerenciaPregunta.aprobarSugerencia(idSugerencia, usuario);
    } catch (IllegalArgumentException e) {
      return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
    }

    return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
  }

  @PostMapping("/admin/sugerencias/eliminar")
  public ModelAndView eliminarSugerencia(
    @RequestParam("id") Long idSugerencia,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    try {
      servicioSugerenciaPregunta.eliminarSugerencia(idSugerencia, usuario);
    } catch (IllegalArgumentException e) {
      return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
    }

    return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
  }

  @GetMapping("/admin/sugerencias/editar")
  public ModelAndView mostrarEditarSugerencia(
    @RequestParam("id") Long idSugerencia,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (!esAdmin(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    SugerenciaPregunta sugerencia = servicioSugerenciaPregunta.buscarPorId(idSugerencia);

    if (sugerencia == null) {
      return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
    }

    ModelMap modelo = new ModelMap();
    cargarDatosFormulario(modelo);
    modelo.put(DATOS_SUGERENCIA, convertirADatos(sugerencia));

    return new ModelAndView(VISTA_ADMIN_EDITAR_SUGERENCIA, modelo);
  }

  @PostMapping("/admin/sugerencias/editar")
  public ModelAndView guardarEdicionSugerencia(
    @ModelAttribute(DATOS_SUGERENCIA) DatosSugerenciaPregunta datos,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    try {
      servicioSugerenciaPregunta.actualizarSugerencia(datos, usuario);
      return new ModelAndView(REDIRECT_ADMIN_SUGERENCIAS);
    } catch (IllegalArgumentException e) {
      ModelMap modelo = new ModelMap();
      modelo.put(ERROR, e.getMessage());
      modelo.put(DATOS_SUGERENCIA, datos);
      cargarDatosFormulario(modelo);
      return new ModelAndView(VISTA_ADMIN_EDITAR_SUGERENCIA, modelo);
    }
  }

  @GetMapping("/admin/preguntas/crear")
  public ModelAndView mostrarFormularioCrearPreguntaAdmin(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    if (!esAdmin(usuario)) {
      return new ModelAndView(REDIRECT_HOME);
    }

    ModelMap modelo = new ModelMap();
    cargarDatosFormulario(modelo);
    modelo.put(DATOS_SUGERENCIA, new DatosSugerenciaPregunta());

    return new ModelAndView(VISTA_ADMIN_CREAR_PREGUNTA, modelo);
  }

  @PostMapping("/admin/preguntas/crear")
  public ModelAndView guardarPreguntaAdmin(
    @ModelAttribute(DATOS_SUGERENCIA) DatosSugerenciaPregunta datos,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    ModelMap modelo = new ModelMap();

    try {
      servicioSugerenciaPregunta.crearPreguntaComoAdmin(datos, usuario);
      modelo.put(EXITO, "¡Pregunta creada correctamente!");
      modelo.put(DATOS_SUGERENCIA, new DatosSugerenciaPregunta());
    } catch (IllegalArgumentException e) {
      modelo.put(ERROR, e.getMessage());
      modelo.put(DATOS_SUGERENCIA, datos);
    }

    cargarDatosFormulario(modelo);

    return new ModelAndView(VISTA_ADMIN_CREAR_PREGUNTA, modelo);
  }

  private void cargarDatosFormulario(ModelMap modelo) {
    modelo.put("provincias", servicioProvincia.obtenerProvincias());
  }

  private DatosSugerenciaPregunta convertirADatos(SugerenciaPregunta sugerencia) {
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    datos.setId(sugerencia.getId());
    datos.setEnunciado(sugerencia.getEnunciado());
    datos.setRespuestaCorrecta(sugerencia.getRespuestaCorrecta());
    datos.setOpcionIncorrectaUno(sugerencia.getOpcionIncorrectaUno());
    datos.setOpcionIncorrectaDos(sugerencia.getOpcionIncorrectaDos());
    datos.setOpcionIncorrectaTres(sugerencia.getOpcionIncorrectaTres());

    if (sugerencia.getProvincia() != null) {
      datos.setIdProvincia(sugerencia.getProvincia().getId());
    }

    return datos;
  }

  private boolean esJugador(Usuario usuario) {
    return tieneRol(usuario, "JUGADOR");
  }

  private boolean esAdmin(Usuario usuario) {
    return tieneRol(usuario, "ADMIN");
  }

  private boolean tieneRol(Usuario usuario, String rolEsperado) {
    return (
      usuario != null &&
      usuario.getRol() != null &&
      usuario.getRol().getDescripcion() != null &&
      usuario.getRol().getDescripcion().equals(rolEsperado)
    );
  }
}
