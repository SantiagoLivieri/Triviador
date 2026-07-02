package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.ServicioSugerenciaPregunta;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorSugerenciaPreguntaTest {

  private ControladorSugerenciaPregunta controlador;

  private ServicioSugerenciaPregunta servicioSugerenciaPregunta;
  private ServicioProvincia servicioProvincia;
  private HttpSession session;

  @BeforeEach
  public void init() {
    servicioSugerenciaPregunta = mock(ServicioSugerenciaPregunta.class);
    servicioProvincia = mock(ServicioProvincia.class);
    session = mock(HttpSession.class);

    controlador = new ControladorSugerenciaPregunta(servicioSugerenciaPregunta, servicioProvincia);
  }

  private Usuario crearUsuarioConRol(String descripcionRol) {
    Rol rol = new Rol();
    rol.setDescripcion(descripcionRol);

    Usuario usuario = new Usuario();
    usuario.setRol(rol);

    return usuario;
  }

  //mostrarFormularioSugerencia    
  @Test
  public void jugadorPuedeVerFormularioDeSugerencia() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");

    when(session.getAttribute("usuarioLogueado")).thenReturn(jugador);

    ModelAndView mv = controlador.mostrarFormularioSugerencia(session);

    assertEquals("sugerir-pregunta", mv.getViewName());
    assertNotNull(mv.getModel().get("datosSugerenciaPregunta"));
  }

  @Test
  public void usuarioSinRolJugadorEsRedirigidoAlHome() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.mostrarFormularioSugerencia(session);

    assertEquals("redirect:/home", mv.getViewName());
  }

  //guardarSugerencia
  @Test
  public void guardarSugerenciaExitosaMuestraMensajeExito() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado")).thenReturn(jugador);

    ModelAndView mv = controlador.guardarSugerencia(datos, session);

    assertEquals("sugerir-pregunta", mv.getViewName());
    assertEquals(
      "¡Tu sugerencia fue enviada y quedó pendiente de aprobación!",
      mv.getModel().get("exito")
    );

    verify(servicioSugerenciaPregunta).crearSugerencia(datos, jugador);
  }

  @Test
  public void guardarSugerenciaConErrorMuestraMensaje() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado")).thenReturn(jugador);

    doThrow(new IllegalArgumentException("error"))
      .when(servicioSugerenciaPregunta)
      .crearSugerencia(datos, jugador);

    ModelAndView mv = controlador.guardarSugerencia(datos, session);

    assertEquals("sugerir-pregunta", mv.getViewName());
    assertEquals("error", mv.getModel().get("error"));
  }

  //mostrarSugerenciasAdmin
  @Test
  public void adminPuedeVerSugerenciasPendientes() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    when(servicioSugerenciaPregunta.obtenerSugerenciasPendientes()).thenReturn(List.of());

    ModelAndView mv = controlador.mostrarSugerenciasAdmin(session);

    assertEquals("admin-sugerencias", mv.getViewName());

    verify(servicioSugerenciaPregunta).obtenerSugerenciasPendientes();
  }

  @Test
  public void noAdminNoPuedeVerSugerencias() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");

    when(session.getAttribute("usuarioLogueado")).thenReturn(jugador);

    ModelAndView mv = controlador.mostrarSugerenciasAdmin(session);

    assertEquals("redirect:/home", mv.getViewName());
  }

  //aprobarSugerencia
  @Test
  public void aprobarSugerenciaRedirecciona() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.aprobarSugerencia(1L, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());

    verify(servicioSugerenciaPregunta).aprobarSugerencia(1L, admin);
  }

  @Test
  public void aprobarSugerenciaConErrorRedireccionaAlListado() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    doThrow(new IllegalArgumentException("error"))
      .when(servicioSugerenciaPregunta)
      .aprobarSugerencia(1L, admin);

    ModelAndView mv = controlador.aprobarSugerencia(1L, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());

    verify(servicioSugerenciaPregunta).aprobarSugerencia(1L, admin);
  }

  //eliminarSugerencia
  @Test
  public void eliminarSugerenciaRedirecciona() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.eliminarSugerencia(1L, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());

    verify(servicioSugerenciaPregunta).eliminarSugerencia(1L, admin);
  }

  @Test
  public void eliminarSugerenciaConErrorRedireccionaAlListado() {

    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado"))
    .thenReturn(admin);

    doThrow(new IllegalArgumentException("error"))
    .when(servicioSugerenciaPregunta)
    .eliminarSugerencia(1L, admin);

    ModelAndView mv = controlador.eliminarSugerencia(1L, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());

    verify(servicioSugerenciaPregunta).eliminarSugerencia(1L, admin);
  }

  //mostrarEditarSugerencia 
  @Test
  public void editarSugerenciaNoAdminRedireccionaHome() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");

    when(session.getAttribute("usuarioLogueado")).thenReturn(jugador);

    ModelAndView mv = controlador.mostrarEditarSugerencia(1L, session);

    assertEquals("redirect:/home", mv.getViewName());
  }

  @Test
  public void editarSugerenciaInexistenteRedireccionaListado() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    when(servicioSugerenciaPregunta.buscarPorId(1L)).thenReturn(null);

    ModelAndView mv = controlador.mostrarEditarSugerencia(1L, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());
  }


  @Test
  public void editarSugerenciaExistenteMuestraVista() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    when(sugerencia.getId()).thenReturn(1L);
    when(sugerencia.getEnunciado()).thenReturn("Capital");
    when(sugerencia.getRespuestaCorrecta()).thenReturn("La Plata");
    when(sugerencia.getOpcionIncorrectaUno()).thenReturn("Córdoba");
    when(sugerencia.getOpcionIncorrectaDos()).thenReturn("Rosario");
    when(sugerencia.getOpcionIncorrectaTres()).thenReturn("Mendoza");
    when(sugerencia.getProvincia()).thenReturn(null);

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    when(servicioSugerenciaPregunta.buscarPorId(1L)).thenReturn(sugerencia);

    ModelAndView mv = controlador.mostrarEditarSugerencia(1L, session);

    DatosSugerenciaPregunta datos = (DatosSugerenciaPregunta) mv
      .getModel()
      .get("datosSugerenciaPregunta");

    assertEquals(1L, datos.getId());
    assertEquals("Capital", datos.getEnunciado());

    assertEquals("admin-editar-sugerencia", mv.getViewName());
  }

  @Test
  public void editarSugerenciaConProvinciaCargaIdProvincia() {

    Usuario admin = crearUsuarioConRol("ADMIN");

    Provincia provincia = mock(Provincia.class);
    when(provincia.getId()).thenReturn(5L);

    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);
    when(sugerencia.getId()).thenReturn(1L);
    when(sugerencia.getEnunciado()).thenReturn("Capital");
    when(sugerencia.getRespuestaCorrecta()).thenReturn("La Plata");
    when(sugerencia.getOpcionIncorrectaUno()).thenReturn("A");
    when(sugerencia.getOpcionIncorrectaDos()).thenReturn("B");
    when(sugerencia.getOpcionIncorrectaTres()).thenReturn("C");
    when(sugerencia.getProvincia()).thenReturn(provincia);

    when(session.getAttribute("usuarioLogueado"))
    .thenReturn(admin);

    when(servicioSugerenciaPregunta.buscarPorId(1L))
    .thenReturn(sugerencia);

    ModelAndView mv = controlador.mostrarEditarSugerencia(1L, session);

    DatosSugerenciaPregunta datos = 
    (DatosSugerenciaPregunta) mv.getModel().get("datosSugerenciaPregunta");
    
    assertEquals(5L, datos.getIdProvincia());
  }

  //guardarEdicionSugerencia
  @Test
  public void guardarEdicionExitosaRedirecciona() {
    Usuario admin = crearUsuarioConRol("ADMIN");
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.guardarEdicionSugerencia(datos, session);

    assertEquals("redirect:/admin/sugerencias", mv.getViewName());
  }

  @Test
  public void guardarEdicionConErrorMuestraFormularioNuevamente() {

    Usuario admin = crearUsuarioConRol("ADMIN");

    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado"))
    .thenReturn(admin);

    doThrow(new IllegalArgumentException("datos inválidos"))
    .when(servicioSugerenciaPregunta)
    .actualizarSugerencia(datos, admin);

    ModelAndView mv = controlador.guardarEdicionSugerencia(datos, session);

    assertEquals("admin-editar-sugerencia", mv.getViewName());

    assertEquals("datos inválidos", mv.getModel().get("error"));

    assertEquals(datos, mv.getModel().get("datosSugerenciaPregunta"));
  }

  //mostrarFormularioCrearPreguntaAdmin
  @Test
  public void mostrarFormularioCrearPreguntaAdmin() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.mostrarFormularioCrearPreguntaAdmin(session);

    assertEquals("admin-crear-pregunta", mv.getViewName());
  }

  @Test
  public void usuarioNoAdminNoPuedeVerFormularioCrearPregunta() {
    Usuario jugador = crearUsuarioConRol("JUGADOR");

    when(session.getAttribute("usuarioLogueado"))
    .thenReturn(jugador);

    ModelAndView mv =controlador.mostrarFormularioCrearPreguntaAdmin(session);

    assertEquals("redirect:/home", mv.getViewName());
  }

  //guardarPreguntaAdmin
  @Test
  public void guardarPreguntaAdminExitosa() {
    Usuario admin = crearUsuarioConRol("ADMIN");

    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado")).thenReturn(admin);

    ModelAndView mv = controlador.guardarPreguntaAdmin(datos, session);

    assertEquals("admin-crear-pregunta", mv.getViewName());

    assertEquals("¡Pregunta creada correctamente!", mv.getModel().get("exito"));
  }

  @Test
  public void guardarPreguntaAdminConErrorMuestraFormulario() {

    Usuario admin = crearUsuarioConRol("ADMIN");

    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    when(session.getAttribute("usuarioLogueado"))
    .thenReturn(admin);

    doThrow(new IllegalArgumentException("pregunta inválida"))
    .when(servicioSugerenciaPregunta)
    .crearPreguntaComoAdmin(datos, admin);

    ModelAndView mv = controlador.guardarPreguntaAdmin(datos, session);

    assertEquals("admin-crear-pregunta", mv.getViewName());

    assertEquals("pregunta inválida", mv.getModel().get("error"));

    assertEquals(datos, mv.getModel().get("datosSugerenciaPregunta"));
  }
}
