package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.EstadoReportePregunta;
import com.tallerwebi.entidades.ReportePregunta;
import com.tallerwebi.entidades.ReportePregunta;
import com.tallerwebi.entidades.RespuestaPartida;
import java.util.List;

public interface RepositorioRespuestaPartida {
  void guardar(RespuestaPartida respuestaPartida);

  void actualizar(RespuestaPartida respuestaPartida);

  RespuestaPartida buscarPorId(Long id);

  RespuestaPartida buscarPorPartidaJugadorPregunta(Long partidaId, Long jugadorId, Long preguntaId);

  List<RespuestaPartida> buscarPorPartidaYUsuario(Long partidaId, Long usuarioId);

  ReportePregunta buscarReportePorRespuestaId(Long respuestaPartidaId);

  void guardarReporte(ReportePregunta reportePregunta);

  List<ReportePregunta> buscarReportesPorEstado(EstadoReportePregunta estado);

  ReportePregunta buscarReportePorId(Long reporteId);

  void actualizarReporte(ReportePregunta reportePregunta);
}
