package com.tallerwebi.servicios.excepcion;

public class UsuarioInexistenteException extends Exception {

  private static final long serialVersionUID = 1L;

  public UsuarioInexistenteException(String message) {
    super(message);
  }
}
