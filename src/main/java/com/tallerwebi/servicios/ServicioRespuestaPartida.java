package com.tallerwebi.servicios;

import com.tallerwebi.entidades.EstadoReportePregunta;
import com.tallerwebi.entidades.MotivoReportePregunta;
import com.tallerwebi.entidades.ReportePregunta;
import com.tallerwebi.entidades.RespuestaPartida;
import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface ServicioRespuestaPartida {
  void registrarOActualizarRespuesta(
    Long partidaId,
    Long preguntaId,
    String respuestaElegida,
    boolean correcta
  );

  List<RespuestaPartida> buscarPorPartidaYUsuario(Long partidaId, Long usuarioId);

  void reportarPregunta(
    Long partidaId,
    Long respuestaPartidaId,
    Long usuarioId,
    MotivoReportePregunta motivo,
    String comentario
  );

  List<ReportePregunta> obtenerReportesPendientes();

  void cambiarEstadoReporte(
    Long reporteId,
    EstadoReportePregunta nuevoEstado,
    Usuario usuarioAdmin
  );
}
