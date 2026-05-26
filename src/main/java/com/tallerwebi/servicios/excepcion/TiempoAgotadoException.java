package com.tallerwebi.servicios.excepcion;

public class TiempoAgotadoException extends Exception {

  private static final long serialVersionUID = 1L;

  public TiempoAgotadoException(String mensaje) {
    super(mensaje);
  }
}
