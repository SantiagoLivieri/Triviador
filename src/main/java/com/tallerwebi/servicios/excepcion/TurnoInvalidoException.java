package com.tallerwebi.servicios.excepcion;

public class TurnoInvalidoException extends Exception {

  private static final long serialVersionUID = 1L;

  public TurnoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
