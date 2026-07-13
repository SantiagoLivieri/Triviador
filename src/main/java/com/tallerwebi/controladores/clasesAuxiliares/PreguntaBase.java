package com.tallerwebi.controladores.clasesAuxiliares;

public class PreguntaBase {

  protected String enunciado;
  protected String respuestaCorrecta;
  protected String opcionIncorrectaUno;
  protected String opcionIncorrectaDos;
  protected String opcionIncorrectaTres;
  protected Long idProvincia;

  public String getEnunciado() {
    return enunciado;
  }

  public void setEnunciado(String enunciado) {
    this.enunciado = enunciado;
  }

  public String getRespuestaCorrecta() {
    return respuestaCorrecta;
  }

  public void setRespuestaCorrecta(String respuestaCorrecta) {
    this.respuestaCorrecta = respuestaCorrecta;
  }

  public String getOpcionIncorrectaUno() {
    return opcionIncorrectaUno;
  }

  public void setOpcionIncorrectaUno(String opcionIncorrectaUno) {
    this.opcionIncorrectaUno = opcionIncorrectaUno;
  }

  public String getOpcionIncorrectaDos() {
    return opcionIncorrectaDos;
  }

  public void setOpcionIncorrectaDos(String opcionIncorrectaDos) {
    this.opcionIncorrectaDos = opcionIncorrectaDos;
  }

  public String getOpcionIncorrectaTres() {
    return opcionIncorrectaTres;
  }

  public void setOpcionIncorrectaTres(String opcionIncorrectaTres) {
    this.opcionIncorrectaTres = opcionIncorrectaTres;
  }

  public Long getIdProvincia() {
    return idProvincia;
  }

  public void setIdProvincia(Long idProvincia) {
    this.idProvincia = idProvincia;
  }
}
