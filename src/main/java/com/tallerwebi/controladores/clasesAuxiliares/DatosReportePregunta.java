package com.tallerwebi.controladores.clasesAuxiliares;

import com.tallerwebi.entidades.MotivoReportePregunta;

public class DatosReportePregunta {

  private Long respuestaPartidaId;
  private MotivoReportePregunta motivo;
  private String comentario;

  public Long getRespuestaPartidaId() {
    return respuestaPartidaId;
  }

  public void setRespuestaPartidaId(Long respuestaPartidaId) {
    this.respuestaPartidaId = respuestaPartidaId;
  }

  public MotivoReportePregunta getMotivo() {
    return motivo;
  }

  public void setMotivo(MotivoReportePregunta motivo) {
    this.motivo = motivo;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }
}
