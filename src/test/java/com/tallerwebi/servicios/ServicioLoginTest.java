package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLogin;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.Impl.ServicioLoginImpl;
import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * JUnit + Mockito:
 *
 * @ExtendWith(MockitoExtension.class) hace que Mockito funcione con JUnit 5.
 *
 * Gracias a esto podemos usar:
 * - @Mock
 * - @InjectMocks
 */
@ExtendWith(MockitoExtension.class)
public class ServicioLoginTest {

  /*
   * Mockito:
   *
   * Mockeamos el repositorio porque en un test unitario
   * no queremos usar base de datos real.
   */
  @Mock
  private RepositorioUsuario repositorioUsuario;

  /*
   * Mockito:
   *
   * Este repositorio se usa al crear usuarios,
   * porque el usuario nuevo necesita un rol.
   */
  @Mock
  private RepositorioRol repositorioRol;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioLoginImpl
   * e inyecta los mocks anteriores en su constructor.
   *
   * Esta es la clase que estamos testeando.
   */
  @InjectMocks
  private ServicioLoginImpl servicioLogin;

  @Test
  public void validarDatosConUsuarioExistenteDebeRetornarUsuario()
    throws UsuarioInexistenteException {
    /*
     * Caso feliz:
     *
     * Si el email y password existen,
     * el repositorio devuelve un Usuario
     * y el servicio debe retornar ese mismo usuario.
     */

    // Preparación
    String email = "test@test.com";
    String password = "123";

    DatosLogin datosLogin = new DatosLogin(email, password);
    Usuario usuarioEsperado = new Usuario();

    when(repositorioUsuario.buscarUsuario(email, password)).thenReturn(usuarioEsperado);

    // Ejecución
    Usuario usuarioObtenido = servicioLogin.validarDatos(datosLogin);

    // Verificación
    assertSame(usuarioEsperado, usuarioObtenido);
    verify(repositorioUsuario).buscarUsuario(email, password);
  }

  @Test
  public void validarDatosConDatosNullDebeLanzarUsuarioInexistenteException() {
    /*
     * Si DatosLogin es null, no se puede validar nada.
     * Debe lanzar UsuarioInexistenteException.
     */

    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(null));

    /*
     * Mockito:
     *
     * Como los datos son null, no debería consultar al repositorio.
     */
    verify(repositorioUsuario, never()).buscarUsuario(any(), any());
  }

  @Test
  public void validarDatosConEmailVacioDebeLanzarUsuarioInexistenteException() {
    DatosLogin datosLogin = new DatosLogin("", "123");

    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(datosLogin));

    verify(repositorioUsuario, never()).buscarUsuario(any(), any());
  }

  @Test
  public void validarDatosConEmailSoloEspaciosDebeLanzarUsuarioInexistenteException() {
    DatosLogin datosLogin = new DatosLogin("   ", "123");

    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(datosLogin));

    verify(repositorioUsuario, never()).buscarUsuario(any(), any());
  }

  @Test
  public void validarDatosConPasswordVaciaDebeLanzarUsuarioInexistenteException() {
    DatosLogin datosLogin = new DatosLogin("test@test.com", "");

    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(datosLogin));

    verify(repositorioUsuario, never()).buscarUsuario(any(), any());
  }

  @Test
  public void validarDatosConPasswordNullDebeLanzarUsuarioInexistenteException() {
    DatosLogin datosLogin = new DatosLogin("test@test.com", null);

    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(datosLogin));

    verify(repositorioUsuario, never()).buscarUsuario(any(), any());
  }

  @Test
  public void validarDatosCuandoRepositorioNoEncuentraUsuarioDebeLanzarUsuarioInexistenteException() {
    /*
     * Caso:
     *
     * Los datos no están vacíos,
     * pero el repositorio no encuentra ningún usuario.
     */

    // Preparación
    String email = "noexiste@test.com";
    String password = "123";
    DatosLogin datosLogin = new DatosLogin(email, password);

    when(repositorioUsuario.buscarUsuario(email, password)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(UsuarioInexistenteException.class, () -> servicioLogin.validarDatos(datosLogin));

    verify(repositorioUsuario).buscarUsuario(email, password);
  }

  @Test
  public void validarEmailConEmailDisponibleNoDebeLanzarExcepcion()
    throws UsuarioExistenteException {
    /*
     * Si el email no está registrado,
     * validarEmail no debe lanzar excepción.
     */

    // Preparación
    String email = "nuevo@test.com";

    when(repositorioUsuario.buscarUsuarioPorEmail(email)).thenReturn(null);

    // Ejecución + Verificación
    assertDoesNotThrow(() -> servicioLogin.validarEmail(email));

    verify(repositorioUsuario).buscarUsuarioPorEmail(email);
  }

  @Test
  public void validarEmailDebeBuscarElEmailSinEspaciosAlInicioYFinal()
    throws UsuarioExistenteException {
    /*
     * ServicioLoginImpl hace email.trim().
     *
     * Este test verifica que si llega "  nuevo@test.com  ",
     * busque "nuevo@test.com".
     */

    // Preparación
    String emailConEspacios = "  nuevo@test.com  ";
    String emailSinEspacios = "nuevo@test.com";

    when(repositorioUsuario.buscarUsuarioPorEmail(emailSinEspacios)).thenReturn(null);

    // Ejecución
    servicioLogin.validarEmail(emailConEspacios);

    // Verificación
    verify(repositorioUsuario).buscarUsuarioPorEmail(emailSinEspacios);
  }

  @Test
  public void validarEmailConEmailNullDebeLanzarIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> servicioLogin.validarEmail(null));

    verify(repositorioUsuario, never()).buscarUsuarioPorEmail(any());
  }

  @Test
  public void validarEmailConEmailVacioDebeLanzarIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> servicioLogin.validarEmail(""));

    verify(repositorioUsuario, never()).buscarUsuarioPorEmail(any());
  }

  @Test
  public void validarEmailConEmailSoloEspaciosDebeLanzarIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> servicioLogin.validarEmail("   "));

    verify(repositorioUsuario, never()).buscarUsuarioPorEmail(any());
  }

  @Test
  public void validarEmailConUsuarioExistenteDebeLanzarUsuarioExistenteException() {
    // Preparación
    String email = "existe@test.com";

    when(repositorioUsuario.buscarUsuarioPorEmail(email)).thenReturn(new Usuario());

    // Ejecución + Verificación
    assertThrows(UsuarioExistenteException.class, () -> servicioLogin.validarEmail(email));

    verify(repositorioUsuario).buscarUsuarioPorEmail(email);
  }

  @Test
  public void validarPasswordConPasswordsIgualesNoDebeLanzarExcepcion() {
    /*
     * Caso feliz:
     *
     * Si password y rePassword coinciden,
     * no debe lanzar excepción.
     */

    assertDoesNotThrow(() -> servicioLogin.validarPassword("123", "123"));
  }

  @Test
  public void validarPasswordConPasswordNullDebeLanzarPasswordsDiferentesException() {
    assertThrows(
      PasswordsDiferentesException.class,
      () -> servicioLogin.validarPassword(null, "123")
    );
  }

  @Test
  public void validarPasswordConPasswordVaciaDebeLanzarPasswordsDiferentesException() {
    assertThrows(
      PasswordsDiferentesException.class,
      () -> servicioLogin.validarPassword("", "123")
    );
  }

  @Test
  public void validarPasswordConPasswordSoloEspaciosDebeLanzarPasswordsDiferentesException() {
    assertThrows(
      PasswordsDiferentesException.class,
      () -> servicioLogin.validarPassword("   ", "123")
    );
  }

  @Test
  public void validarPasswordConPasswordsDistintasDebeLanzarPasswordsDiferentesException() {
    assertThrows(
      PasswordsDiferentesException.class,
      () -> servicioLogin.validarPassword("123", "456")
    );
  }

  @Test
  public void validarPasswordConRePasswordNullDebeLanzarPasswordsDiferentesException() {
    assertThrows(
      PasswordsDiferentesException.class,
      () -> servicioLogin.validarPassword("123", null)
    );
  }

  @Test
  public void crearUsuarioDebeBuscarRolJugadorYGuardarUsuario() {
    /*
     * crearUsuario() hace dos cosas:
     *
     * 1. Busca el rol "JUGADOR"
     * 2. Crea un usuario nuevo usando DatosRegistro y ese rol
     * 3. Lo guarda en RepositorioUsuario
     */

    // Preparación
    DatosRegistro datosRegistro = new DatosRegistro("Fabri", "nuevo@test.com", "123", "123");

    Rol rolJugador = new Rol();
    when(repositorioRol.buscarPorDescripcion("JUGADOR")).thenReturn(rolJugador);

    // Ejecución
    servicioLogin.crearUsuario(datosRegistro);

    // Verificación
    verify(repositorioRol).buscarPorDescripcion("JUGADOR");
    verify(repositorioUsuario).crearUsuario(any(Usuario.class));
  }
}
