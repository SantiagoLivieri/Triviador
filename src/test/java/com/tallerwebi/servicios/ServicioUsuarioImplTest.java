package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.Impl.ServicioUsuarioImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioUsuarioImplTest {

  @Mock
  private RepositorioUsuario repositorioUsuario;

  @Mock
  private RepositorioRol repositorioRol;

  @InjectMocks
  private ServicioUsuarioImpl servicioUsuario;

  @Test
  public void alBuscarUsuarioPorIdDebeRetornarUsuarioDelRepositorio() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuarioEsperado = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioEsperado);

    // Ejecución
    Usuario usuarioObtenido = servicioUsuario.buscarUsuarioPorId(usuarioId);

    // Verificación
    assertSame(usuarioEsperado, usuarioObtenido);
    verify(repositorioUsuario).buscarUsuarioPorId(usuarioId);
  }

  @Test
  public void alActualizarPerfilConDatosValidosDebeActualizarNombreNombreJugadorYGuardar() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución
    servicioUsuario.actualizarPerfil(usuarioId, "Santiago", "Santi");

    // Verificación
    assertEquals("Santiago", usuario.getNombre());
    assertEquals("Santi", usuario.getNombreJugador());

    verify(repositorioUsuario).buscarUsuarioPorId(usuarioId);
    verify(repositorioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void alActualizarPerfilDeUsuarioInexistenteDebeLanzarIllegalArgumentException() {
    // Preparación
    Long usuarioId = 99L;

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioUsuario.actualizarPerfil(usuarioId, "Santiago", "Santi")
    );

    verify(repositorioUsuario).buscarUsuarioPorId(usuarioId);
    verify(repositorioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void alActualizarPerfilConNombreNullDebeLanzarIllegalArgumentException() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioUsuario.actualizarPerfil(usuarioId, null, "Santi")
    );

    verify(repositorioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void alActualizarPerfilConNombreVacioDebeLanzarIllegalArgumentException() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioUsuario.actualizarPerfil(usuarioId, "   ", "Santi")
    );

    verify(repositorioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void alActualizarPerfilConNombreJugadorNullDebeLanzarIllegalArgumentException() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioUsuario.actualizarPerfil(usuarioId, "Santiago", null)
    );

    verify(repositorioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void alActualizarPerfilConNombreJugadorVacioDebeLanzarIllegalArgumentException() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioUsuario.actualizarPerfil(usuarioId, "Santiago", "   ")
    );

    verify(repositorioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void alCargarRolesInicialesSiNoExistenDebeGuardarJugadorEditorYAdmin() {
    /*
     * Cubre cargarRolesIniciales()
     * cuando no existen los roles.
     *
     * Como buscarPorDescripcion devuelve null,
     * el servicio debe guardar los tres roles iniciales.
     */

    // Preparación
    when(repositorioRol.buscarPorDescripcion("JUGADOR")).thenReturn(null);
    when(repositorioRol.buscarPorDescripcion("EDITOR")).thenReturn(null);
    when(repositorioRol.buscarPorDescripcion("ADMIN")).thenReturn(null);

    // Ejecución
    servicioUsuario.cargarRolesIniciales();

    // Verificación
    ArgumentCaptor<Rol> captorRol = ArgumentCaptor.forClass(Rol.class);

    verify(repositorioRol).buscarPorDescripcion("JUGADOR");
    verify(repositorioRol).buscarPorDescripcion("EDITOR");
    verify(repositorioRol).buscarPorDescripcion("ADMIN");

    verify(repositorioRol, times(3)).guardar(captorRol.capture());

    List<Rol> rolesGuardados = captorRol.getAllValues();

    assertEquals("JUGADOR", rolesGuardados.get(0).getDescripcion());
    assertEquals("EDITOR", rolesGuardados.get(1).getDescripcion());
    assertEquals("ADMIN", rolesGuardados.get(2).getDescripcion());
  }

  @Test
  public void alCargarRolesInicialesSiYaExistenNoDebeGuardarlos() {
    /*
     * Cubre la otra rama de crearRolSiNoExiste().
     *
     * Si el rol ya existe, no debe guardarse nuevamente.
     */

    // Preparación
    when(repositorioRol.buscarPorDescripcion("JUGADOR")).thenReturn(new Rol("JUGADOR"));
    when(repositorioRol.buscarPorDescripcion("EDITOR")).thenReturn(new Rol("EDITOR"));
    when(repositorioRol.buscarPorDescripcion("ADMIN")).thenReturn(new Rol("ADMIN"));

    // Ejecución
    servicioUsuario.cargarRolesIniciales();

    // Verificación
    verify(repositorioRol).buscarPorDescripcion("JUGADOR");
    verify(repositorioRol).buscarPorDescripcion("EDITOR");
    verify(repositorioRol).buscarPorDescripcion("ADMIN");

    verify(repositorioRol, never()).guardar(any(Rol.class));
  }

  @Test
  public void alActualizarUsuarioDebeLlamarAlRepositorio() {
    // Preparación
    Usuario usuario = new Usuario();

    // Ejecución
    servicioUsuario.actualizarUsuario(usuario);

    // Verificación
    verify(repositorioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void alCargarUsuarioAdminInicialSiYaExisteNoDebeCrearOtro() {
    /*
     * Cubre esta rama:
     *
     * if (adminExistente != null) {
     *   return;
     * }
     */

    // Preparación
    Usuario adminExistente = new Usuario();

    when(repositorioUsuario.buscarUsuarioPorEmail("admin@triviador.com"))
      .thenReturn(adminExistente);

    // Ejecución
    servicioUsuario.cargarUsuarioAdminInicial();

    // Verificación
    verify(repositorioUsuario).buscarUsuarioPorEmail("admin@triviador.com");
    verify(repositorioRol, never()).buscarPorDescripcion("ADMIN");
    verify(repositorioUsuario, never()).crearUsuario(any(Usuario.class));
  }

  @Test
  public void alCargarUsuarioAdminInicialSiNoExisteYNoExisteRolAdminDebeLanzarIllegalStateException() {
    /*
     * Cubre esta rama:
     *
     * if (rolAdmin == null)
     */

    // Preparación
    when(repositorioUsuario.buscarUsuarioPorEmail("admin@triviador.com")).thenReturn(null);
    when(repositorioRol.buscarPorDescripcion("ADMIN")).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(IllegalStateException.class, () -> servicioUsuario.cargarUsuarioAdminInicial());

    verify(repositorioUsuario).buscarUsuarioPorEmail("admin@triviador.com");
    verify(repositorioRol).buscarPorDescripcion("ADMIN");
    verify(repositorioUsuario, never()).crearUsuario(any(Usuario.class));
  }

  @Test
  public void alCargarUsuarioAdminInicialSiNoExisteYExisteRolAdminDebeCrearUsuarioAdmin() {
    /*
     * Cubre el caso feliz:
     *
     * No existe usuario admin.
     * Sí existe rol ADMIN.
     * Entonces crea el usuario admin inicial.
     */

    // Preparación
    Rol rolAdmin = new Rol("ADMIN");

    when(repositorioUsuario.buscarUsuarioPorEmail("admin@triviador.com")).thenReturn(null);
    when(repositorioRol.buscarPorDescripcion("ADMIN")).thenReturn(rolAdmin);

    // Ejecución
    servicioUsuario.cargarUsuarioAdminInicial();

    // Verificación
    ArgumentCaptor<Usuario> captorUsuario = ArgumentCaptor.forClass(Usuario.class);

    verify(repositorioUsuario).buscarUsuarioPorEmail("admin@triviador.com");
    verify(repositorioRol).buscarPorDescripcion("ADMIN");
    verify(repositorioUsuario).crearUsuario(captorUsuario.capture());

    Usuario adminCreado = captorUsuario.getValue();

    assertEquals("admin@triviador.com", adminCreado.getEmail());
    assertEquals("admin123", adminCreado.getPassword());
    assertEquals("Administrador", adminCreado.getNombre());
    assertEquals("Admin", adminCreado.getNombreJugador());
    assertSame(rolAdmin, adminCreado.getRol());
  }
}
