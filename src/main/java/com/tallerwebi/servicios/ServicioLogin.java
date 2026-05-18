package com.tallerwebi.servicios;

import com.tallerwebi.controladores.DatosLogin;
import com.tallerwebi.controladores.DatosRegistro;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.excepcion.PasswordsDiferentesException;
import com.tallerwebi.servicios.excepcion.UsuarioExistenteException;
import com.tallerwebi.servicios.excepcion.UsuarioInexistenteException;

public interface ServicioLogin {
  Usuario validarDatos(DatosLogin datosLogin) throws UsuarioInexistenteException;

  void validarEmail(String email) throws UsuarioExistenteException;

  void validarPassword(String password, String rePassword) throws PasswordsDiferentesException;

  void crearUsuario(DatosRegistro datosRegistro);
}
