package com.tallerwebi.controladores.clasesAuxiliares;

public class DatosSugerenciaPregunta {

  private Long id;
  private String enunciado;
  private String respuestaCorrecta;
  private String opcionIncorrectaUno;
  private String opcionIncorrectaDos;
  private String opcionIncorrectaTres;
  private Long idProvincia;

  public DatosSugerenciaPregunta() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
