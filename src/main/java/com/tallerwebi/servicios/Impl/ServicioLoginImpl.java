package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLogin;
import com.tallerwebi.controladores.clasesAuxiliares.DatosRegistro;
import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.ServicioLogin;
import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

  private final RepositorioUsuario repositorioUsuario;
  private final RepositorioRol repositorioRol;

  @Autowired
  public ServicioLoginImpl(RepositorioUsuario repositorioUsuario, RepositorioRol repositorioRol) {
    this.repositorioUsuario = repositorioUsuario;
    this.repositorioRol = repositorioRol;
  }

  @Override
  public Usuario validarDatos(DatosLogin datosLogin) throws UsuarioInexistenteException {
    if (
      datosLogin == null || estaVacio(datosLogin.getEmail()) || estaVacio(datosLogin.getPassword())
    ) {
      throw new UsuarioInexistenteException("Los datos de acceso no pueden estar vacíos.");
    }
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
    if (estaVacio(email)) {
      throw new IllegalArgumentException("El email no puede ser vacío.");
    }
    Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email.trim());
    if (usuario != null) {
      throw new UsuarioExistenteException("Ya existe un usuario registrado con este email.");
    }
  }

  @Override
  public void validarPassword(String password, String rePassword)
    throws PasswordsDiferentesException {
    if (estaVacio(password) || !password.equals(rePassword)) {
      throw new PasswordsDiferentesException(
        "Las contraseñas introducidas no coinciden o son inválidas."
      );
    }
  }

  @Override
  public void crearUsuario(DatosRegistro datosRegistro) {
    // Rol rolUser = repositorioRol.buscarRolPorId(2L);
    Rol rolUser = repositorioRol.buscarPorDescripcion("JUGADOR");
    repositorioUsuario.crearUsuario(new Usuario(datosRegistro, rolUser));
  }

  private boolean estaVacio(String texto) {
    return texto == null || texto.trim().isEmpty();
  }
}
