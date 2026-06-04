package com.tallerwebi.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLogin;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.Impl.ServicioLoginImpl;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*;

public class ServicioLoginTest {

  private ServicioLogin servicioLogin;
  private RepositorioUsuario repositorioUsuarioMock;
  private RepositorioRol repositorioRolMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.repositorioRolMock = mock(RepositorioRol.class);
    this.servicioLogin = new ServicioLoginImpl(this.repositorioUsuarioMock, repositorioRolMock);
  }

  @Test
  public void consultarUsuarioDeberiaLlamarAlRepositorio() throws UsuarioInexistenteException {
    String email = "Test@test.com";
    String password = "123";

    Usuario usuarioEsperado = new Usuario();
    when(this.repositorioUsuarioMock.buscarUsuario(email, password)).thenReturn(usuarioEsperado);

    Usuario usuarioObtenido = this.servicioLogin.validarDatos(new DatosLogin(email, password));

    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarUsuario(email, password);
  }

  @Test
  public void registrarUsuarioSiNoExisteDeberiaGuardarlo() {
    this.servicioLogin.crearUsuario(new DatosRegistro("Fabri", "nuevo@Test.com", "123", "123"));

    verify(this.repositorioUsuarioMock, times(1)).crearUsuario(any(Usuario.class));
  }

  @Test
  public void registrarUsuarioSiExisteDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setEmail("existe@Test.com");
    usuario.setPassword("123");

    when(this.repositorioUsuarioMock.buscarUsuarioPorEmail(usuario.getEmail()))
      .thenReturn(new Usuario());

    assertThrows(
      UsuarioExistenteException.class,
      () -> this.servicioLogin.validarEmail(usuario.getEmail())
    );
  }
}
