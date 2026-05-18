package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLogin;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioUsuario;
import javax.transaction.Transactional;

import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Usuario validarDatos(DatosLogin datosLogin) throws UsuarioInexistenteException {
    Usuario usuario = repositorioUsuario.buscarUsuario(
      datosLogin.getEmail(),
      datosLogin.getPassword()
    );
    if (usuario == null) {
      throw new UsuarioInexistenteException("No se encontro usuarios con esos datos");
    }
    return usuario;
  }

  @Override
  public void validarEmail(String email) throws UsuarioExistenteException {
    Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email);
    if (usuario != null) {
      throw new UsuarioExistenteException("Hay un usuario con este email");
    }
  }

  @Override
  public void validarPassword(String password, String rePassword)
    throws PasswordsDiferentesException {
    if (!password.equals(rePassword)) {
      throw new PasswordsDiferentesException("Las contraseñas son diferentes");
    }
  }

  @Override
  public void crearUsuario(DatosRegistro datosRegistro) {
    repositorioUsuario.crearUsuario(new Usuario(datosRegistro));
  }
}
