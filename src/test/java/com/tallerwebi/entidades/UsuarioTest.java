package com.tallerwebi.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

  @Test
  public void queSePuedaCrearUnUsuarioConConstructorVacio() {
    Usuario usuario = new Usuario();

    assertThat(usuario, is(notNullValue()));
    assertThat(usuario.getId(), is(nullValue()));
    assertThat(usuario.getPuntaje(), is(equalTo(0)));
  }

  @Test
  public void queSePuedaCrearUnUsuarioDesdeDatosRegistroYRol() {
    DatosRegistro datosMock = mock(DatosRegistro.class);
    when(datosMock.getNombre()).thenReturn("Carlos");
    when(datosMock.getEmail()).thenReturn("carlos@mail.com");
    when(datosMock.getPassword()).thenReturn("secreta123");

    Rol rolMock = mock(Rol.class);

    Usuario usuario = new Usuario(datosMock, rolMock);

    assertThat(usuario.getNombre(), is(equalTo("Carlos")));
    assertThat(usuario.getEmail(), is(equalTo("carlos@mail.com")));
    assertThat(usuario.getPassword(), is(equalTo("secreta123")));
    assertThat(usuario.getActivo(), is(true));
    assertThat(usuario.getRol(), is(equalTo(rolMock)));
    assertThat(usuario.getNombreJugador(), is(equalTo("Carlos")));
    assertThat(usuario.getPuntaje(), is(equalTo(0)));
  }

  @Test
  public void queSePuedanModificarLosAtributosDeUnUsuario() {
    Usuario usuario = new Usuario();
    Rol rol = new Rol("ADMIN");

    usuario.setId(10L);
    usuario.setNombre("Maria");
    usuario.setEmail("maria@mail.com");
    usuario.setPassword("pass456");
    usuario.setActivo(false);
    usuario.setNombreJugador("Mary");
    usuario.setColorAsignado("rojo");
    usuario.setPuntaje(500);
    usuario.setRol(rol);

    assertThat(usuario.getId(), is(equalTo(10L)));
    assertThat(usuario.getNombre(), is(equalTo("Maria")));
    assertThat(usuario.getEmail(), is(equalTo("maria@mail.com")));
    assertThat(usuario.getPassword(), is(equalTo("pass456")));
    assertThat(usuario.getActivo(), is(false));
    assertThat(usuario.getNombreJugador(), is(equalTo("Mary")));
    assertThat(usuario.getColorAsignado(), is(equalTo("rojo")));
    assertThat(usuario.getPuntaje(), is(equalTo(500)));
    assertThat(usuario.getRol(), is(equalTo(rol)));
  }

  @Test
  public void queElMetodoActivarPongaElEstadoEnTrue() {
    Usuario usuario = new Usuario();
    usuario.setActivo(false);

    usuario.activar();

    assertThat(usuario.getActivo(), is(true));
  }
}
