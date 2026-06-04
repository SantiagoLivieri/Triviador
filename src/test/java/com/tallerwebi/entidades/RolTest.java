package com.tallerwebi.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class RolTest {

  @Test
  public void queSePuedaCrearUnRolConConstructorVacio() {
    Rol rol = new Rol();

    assertThat(rol, is(notNullValue()));
    assertThat(rol.getId(), is(nullValue()));
    assertThat(rol.getDescripcion(), is(nullValue()));
  }

  @Test
  public void queSePuedaCrearUnRolConConstructorParametrizado() {
    String descripcion = "ADMINISTRADOR";

    Rol rol = new Rol(descripcion);

    assertThat(rol, is(notNullValue()));
    assertThat(rol.getId(), is(nullValue()));
    assertThat(rol.getDescripcion(), is(equalTo(descripcion)));
  }

  @Test
  public void queSePuedanModificarLosAtributosDeUnRol() {
    Rol rol = new Rol();

    rol.setId(1L);
    rol.setDescripcion("USUARIO_ESTANDAR");

    assertThat(rol.getId(), is(equalTo(1L)));
    assertThat(rol.getDescripcion(), is(equalTo("USUARIO_ESTANDAR")));
  }
}
