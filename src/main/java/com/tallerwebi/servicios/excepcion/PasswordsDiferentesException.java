package com.tallerwebi.servicios.excepcion;

public class PasswordsDiferentesException extends Exception {

  private static final long serialVersionUID = 1L;

  public PasswordsDiferentesException(String message) {
    super(message);
  }
}
