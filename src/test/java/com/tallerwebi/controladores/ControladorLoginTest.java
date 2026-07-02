package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLogin;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioLogin;
import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
public class ControladorLoginTest {

  @Mock
  private ServicioLogin servicioLogin;

  @Mock
  private HttpSession session;

  @InjectMocks
  private ControladorLogin controladorLogin;

  @Test
  public void alEntrarALoginDebeRetornarVistaLoginConDatosLogin() {
    ModelAndView modelAndView = controladorLogin.login();

    assertEquals("login", modelAndView.getViewName());
    assertInstanceOf(DatosLogin.class, modelAndView.getModel().get("datosLogin"));
  }

  @Test
  public void alValidarLoginCorrectoDebeGuardarUsuarioEnSesionYRedirigirAHome()
    throws UsuarioInexistenteException {
    // Preparación
    DatosLogin datosLogin = new DatosLogin();

    Usuario usuario = new Usuario();
    usuario.setId(1L);

    when(servicioLogin.validarDatos(datosLogin)).thenReturn(usuario);

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarLogin(datosLogin, session);

    // Verificación
    assertEquals("redirect:/home", modelAndView.getViewName());

    verify(servicioLogin).validarDatos(datosLogin);
    verify(session).setAttribute("usuarioLogueado", usuario);
    verify(session).setAttribute("usuarioId", 1L);
  }

  @Test
  public void alValidarLoginIncorrectoDebeRetornarVistaLoginConError()
    throws UsuarioInexistenteException {
    // Preparación
    DatosLogin datosLogin = new DatosLogin();

    when(servicioLogin.validarDatos(datosLogin))
      .thenThrow(new UsuarioInexistenteException("Usuario inexistente"));

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarLogin(datosLogin, session);

    // Verificación
    assertEquals("login", modelAndView.getViewName());
    assertEquals("Usuario inexistente", modelAndView.getModel().get("error"));

    verify(servicioLogin).validarDatos(datosLogin);
  }

  @Test
  public void alEntrarAHomeDebeRetornarVistaHomeConUsuarioLogueado() {
    // Preparación
    Usuario usuario = new Usuario();

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuario);

    // Ejecución
    ModelAndView modelAndView = controladorLogin.home(session);

    // Verificación
    assertEquals("home", modelAndView.getViewName());
    assertSame(usuario, modelAndView.getModel().get("usuario"));

    verify(session).getAttribute("usuarioLogueado");
  }

  @Test
  public void alCerrarSesionDebeInvalidarSesionYRedirigirALogin() {
    ModelAndView modelAndView = controladorLogin.logout(session);

    assertEquals("redirect:/login", modelAndView.getViewName());
    verify(session).invalidate();
  }

  @Test
  public void alEntrarARegistroDebeRetornarVistaRegistroConDatosRegistro() {
    ModelAndView modelAndView = controladorLogin.registrar();

    assertEquals("registro", modelAndView.getViewName());
    assertInstanceOf(DatosRegistro.class, modelAndView.getModel().get("datosRegistro"));
  }

  @Test
  public void alValidarRegistroCorrectoDebeCrearUsuarioYRedirigirALogin()
    throws UsuarioExistenteException, PasswordsDiferentesException {
    // Preparación
    DatosRegistro datosRegistro = org.mockito.Mockito.mock(DatosRegistro.class);

    when(datosRegistro.getEmail()).thenReturn("santi@test.com");
    when(datosRegistro.getPassword()).thenReturn("123456");
    when(datosRegistro.getRePassword()).thenReturn("123456");

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarRegistro(datosRegistro);

    // Verificación
    assertEquals("redirect:/login", modelAndView.getViewName());

    verify(servicioLogin).validarEmail("santi@test.com");
    verify(servicioLogin).validarPassword("123456", "123456");
    verify(servicioLogin).crearUsuario(datosRegistro);
  }

  @Test
  public void alValidarRegistroConUsuarioExistenteDebeRetornarRegistroConError()
    throws UsuarioExistenteException {
    // Preparación
    DatosRegistro datosRegistro = org.mockito.Mockito.mock(DatosRegistro.class);

    when(datosRegistro.getEmail()).thenReturn("santi@test.com");

    doThrow(new UsuarioExistenteException("El usuario ya existe"))
      .when(servicioLogin)
      .validarEmail("santi@test.com");

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarRegistro(datosRegistro);

    // Verificación
    assertEquals("registro", modelAndView.getViewName());
    assertEquals("El usuario ya existe", modelAndView.getModel().get("error"));

    verify(servicioLogin).validarEmail("santi@test.com");
  }

  @Test
  public void alValidarRegistroConPasswordsDiferentesDebeRetornarRegistroConError()
    throws UsuarioExistenteException, PasswordsDiferentesException {
    // Preparación
    DatosRegistro datosRegistro = org.mockito.Mockito.mock(DatosRegistro.class);

    when(datosRegistro.getEmail()).thenReturn("santi@test.com");
    when(datosRegistro.getPassword()).thenReturn("123456");
    when(datosRegistro.getRePassword()).thenReturn("654321");

    doThrow(new PasswordsDiferentesException("Las contraseñas no coinciden"))
      .when(servicioLogin)
      .validarPassword("123456", "654321");

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarRegistro(datosRegistro);

    // Verificación
    assertEquals("registro", modelAndView.getViewName());
    assertEquals("Las contraseñas no coinciden", modelAndView.getModel().get("error"));

    verify(servicioLogin).validarEmail("santi@test.com");
    verify(servicioLogin).validarPassword("123456", "654321");
  }

  @Test
  public void alValidarRegistroConDatosInvalidosDebeRetornarRegistroConError()
    throws UsuarioExistenteException, PasswordsDiferentesException {
    // Preparación
    DatosRegistro datosRegistro = org.mockito.Mockito.mock(DatosRegistro.class);

    when(datosRegistro.getEmail()).thenReturn("santi@test.com");
    when(datosRegistro.getPassword()).thenReturn("123");
    when(datosRegistro.getRePassword()).thenReturn("123");

    doThrow(new IllegalArgumentException("Datos inválidos"))
      .when(servicioLogin)
      .crearUsuario(datosRegistro);

    // Ejecución
    ModelAndView modelAndView = controladorLogin.validarRegistro(datosRegistro);

    // Verificación
    assertEquals("registro", modelAndView.getViewName());
    assertEquals("Datos inválidos", modelAndView.getModel().get("error"));

    verify(servicioLogin).validarEmail("santi@test.com");
    verify(servicioLogin).validarPassword("123", "123");
    verify(servicioLogin).crearUsuario(datosRegistro);
  }
}
