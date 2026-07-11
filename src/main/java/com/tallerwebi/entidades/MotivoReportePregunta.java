package com.tallerwebi.entidades;

public enum MotivoReportePregunta {
  RESPUESTA_INCORRECTA("La respuesta correcta parece incorrecta"),
  PREGUNTA_AMBIGUA("La pregunta es ambigua"),
  PREGUNTA_DESACTUALIZADA("La pregunta está desactualizada"),
  ERROR_DE_REDACCION("La pregunta tiene un error de redacción"),
  OTRO("Otro motivo");

  private final String descripcion;

  MotivoReportePregunta(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
