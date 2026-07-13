package com.tallerwebi.controladores;

public class GeminiDto {

  private String pregunta;
  private String reglaAdicional;
  private String respuesta;
  private String contextoActual;

  public GeminiDto() {}

  public String getPregunta() {
    return pregunta;
  }

  public void setPregunta(String pregunta) {
    this.pregunta = pregunta;
  }

  public String getReglaAdicional() {
    return reglaAdicional;
  }

  public void setReglaAdicional(String reglaAdicional) {
    this.reglaAdicional = reglaAdicional;
  }

  public String getRespuesta() {
    return respuesta;
  }

  public void setRespuesta(String respuesta) {
    this.respuesta = respuesta;
  }

  public String getContextoActual() {
    return contextoActual;
  }

  public void setContextoActual(String contextoActual) {
    this.contextoActual = contextoActual;
  }
}
