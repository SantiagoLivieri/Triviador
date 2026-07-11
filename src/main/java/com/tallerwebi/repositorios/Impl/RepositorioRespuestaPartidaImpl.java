package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.EstadoReportePregunta;
import com.tallerwebi.entidades.ReportePregunta;
import com.tallerwebi.entidades.RespuestaPartida;
import com.tallerwebi.repositorios.RepositorioRespuestaPartida;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioRespuestaPartida")
public class RepositorioRespuestaPartidaImpl implements RepositorioRespuestaPartida {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioRespuestaPartidaImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(RespuestaPartida respuestaPartida) {
    sessionFactory.getCurrentSession().save(respuestaPartida);
  }

  @Override
  public void actualizar(RespuestaPartida respuestaPartida) {
    sessionFactory.getCurrentSession().update(respuestaPartida);
  }

  @Override
  public RespuestaPartida buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(RespuestaPartida.class, id);
  }

  @Override
  public RespuestaPartida buscarPorPartidaJugadorPregunta(
    Long partidaId,
    Long jugadorId,
    Long preguntaId
  ) {
    String consulta =
      "SELECT respuesta " +
      "FROM RespuestaPartida respuesta " +
      "WHERE respuesta.partida.id = :partidaId " +
      "AND respuesta.jugador.id = :jugadorId " +
      "AND respuesta.pregunta.id = :preguntaId";

    return sessionFactory
      .getCurrentSession()
      .createQuery(consulta, RespuestaPartida.class)
      .setParameter("partidaId", partidaId)
      .setParameter("jugadorId", jugadorId)
      .setParameter("preguntaId", preguntaId)
      .uniqueResult();
  }

  @Override
  public List<RespuestaPartida> buscarPorPartidaYUsuario(Long partidaId, Long usuarioId) {
    String consulta =
      "SELECT DISTINCT respuesta " +
      "FROM RespuestaPartida respuesta " +
      "LEFT JOIN FETCH respuesta.reporte " +
      "WHERE respuesta.partida.id = :partidaId " +
      "AND respuesta.usuario.id = :usuarioId " +
      "ORDER BY respuesta.fechaRespuesta ASC";

    return sessionFactory
      .getCurrentSession()
      .createQuery(consulta, RespuestaPartida.class)
      .setParameter("partidaId", partidaId)
      .setParameter("usuarioId", usuarioId)
      .getResultList();
  }

  @Override
  public ReportePregunta buscarReportePorRespuestaId(Long respuestaPartidaId) {
    String consulta =
      "SELECT reporte " +
      "FROM ReportePregunta reporte " +
      "WHERE reporte.respuestaPartida.id = :respuestaPartidaId";

    return sessionFactory
      .getCurrentSession()
      .createQuery(consulta, ReportePregunta.class)
      .setParameter("respuestaPartidaId", respuestaPartidaId)
      .uniqueResult();
  }

  @Override
  public void guardarReporte(ReportePregunta reportePregunta) {
    sessionFactory.getCurrentSession().save(reportePregunta);
  }

  @Override
  public List<ReportePregunta> buscarReportesPorEstado(EstadoReportePregunta estado) {
    String consulta =
      "SELECT DISTINCT reporte " +
      "FROM ReportePregunta reporte " +
      "JOIN FETCH reporte.respuestaPartida respuesta " +
      "JOIN FETCH respuesta.pregunta pregunta " +
      "JOIN FETCH respuesta.partida partida " +
      "JOIN FETCH reporte.usuario usuario " +
      "WHERE reporte.estado = :estado " +
      "ORDER BY reporte.fechaReporte ASC";

    return sessionFactory
      .getCurrentSession()
      .createQuery(consulta, ReportePregunta.class)
      .setParameter("estado", estado)
      .getResultList();
  }

  @Override
  public ReportePregunta buscarReportePorId(Long reporteId) {
    String consulta =
      "SELECT reporte " +
      "FROM ReportePregunta reporte " +
      "JOIN FETCH reporte.respuestaPartida respuesta " +
      "JOIN FETCH respuesta.pregunta pregunta " +
      "JOIN FETCH respuesta.partida partida " +
      "JOIN FETCH reporte.usuario usuario " +
      "WHERE reporte.id = :reporteId";

    return sessionFactory
      .getCurrentSession()
      .createQuery(consulta, ReportePregunta.class)
      .setParameter("reporteId", reporteId)
      .uniqueResult();
  }

  @Override
  public void actualizarReporte(ReportePregunta reportePregunta) {
    sessionFactory.getCurrentSession().update(reportePregunta);
  }
}
