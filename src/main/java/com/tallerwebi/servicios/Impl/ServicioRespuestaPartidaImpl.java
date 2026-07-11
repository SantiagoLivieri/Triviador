package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.EstadoDePartida;
import com.tallerwebi.entidades.EstadoReportePregunta;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.MotivoReportePregunta;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.ReportePregunta;
import com.tallerwebi.entidades.RespuestaPartida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPartida;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioRespuestaPartida;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.ServicioRespuestaPartida;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioRespuestaPartida")
@Transactional
public class ServicioRespuestaPartidaImpl implements ServicioRespuestaPartida {

  private static final int LONGITUD_MAXIMA_COMENTARIO = 1000;
  private static final String ROL_ADMIN = "ADMIN";

  private final RepositorioRespuestaPartida repositorioRespuestaPartida;
  private final RepositorioPartida repositorioPartida;
  private final RepositorioPregunta repositorioPregunta;
  private final RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioRespuestaPartidaImpl(
    RepositorioRespuestaPartida repositorioRespuestaPartida,
    RepositorioPartida repositorioPartida,
    RepositorioPregunta repositorioPregunta,
    RepositorioUsuario repositorioUsuario
  ) {
    this.repositorioRespuestaPartida = repositorioRespuestaPartida;
    this.repositorioPartida = repositorioPartida;
    this.repositorioPregunta = repositorioPregunta;
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public void registrarOActualizarRespuesta(
    Long partidaId,
    Long preguntaId,
    String respuestaElegida,
    boolean correcta
  ) {
    validarDatosDeRespuesta(partidaId, preguntaId, respuestaElegida);

    Partida partida = obtenerPartida(partidaId);
    Jugador jugador = obtenerJugadorActual(partida);

    RespuestaPartida respuestaExistente =
      repositorioRespuestaPartida.buscarPorPartidaJugadorPregunta(
        partidaId,
        jugador.getId(),
        preguntaId
      );

    if (respuestaExistente == null) {
      Pregunta pregunta = obtenerPregunta(preguntaId);

      guardarNuevaRespuesta(partida, pregunta, jugador, respuestaElegida, correcta);
      return;
    }

    actualizarRespuestaExistente(respuestaExistente, respuestaElegida, correcta);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RespuestaPartida> buscarPorPartidaYUsuario(Long partidaId, Long usuarioId) {
    if (partidaId == null || usuarioId == null) {
      throw new IllegalArgumentException("La partida y el usuario son obligatorios.");
    }

    return repositorioRespuestaPartida.buscarPorPartidaYUsuario(partidaId, usuarioId);
  }

  @Override
  public void reportarPregunta(
    Long partidaId,
    Long respuestaPartidaId,
    Long usuarioId,
    MotivoReportePregunta motivo,
    String comentario
  ) {
    validarDatosDeReporte(partidaId, respuestaPartidaId, usuarioId, motivo);

    RespuestaPartida respuesta = obtenerRespuestaPartida(respuestaPartidaId);

    validarRespuestaDePartida(respuesta, partidaId);
    validarPartidaFinalizada(respuesta.getPartida());
    validarPropietarioDeRespuesta(respuesta, usuarioId);
    validarReporteInexistente(respuestaPartidaId);

    Usuario usuario = obtenerUsuario(usuarioId);
    String comentarioNormalizado = normalizarComentario(comentario);

    ReportePregunta reportePregunta = new ReportePregunta(
      respuesta,
      usuario,
      motivo,
      comentarioNormalizado
    );

    repositorioRespuestaPartida.guardarReporte(reportePregunta);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReportePregunta> obtenerReportesPendientes() {
    return repositorioRespuestaPartida.buscarReportesPorEstado(EstadoReportePregunta.PENDIENTE);
  }

  @Override
  public void cambiarEstadoReporte(
    Long reporteId,
    EstadoReportePregunta nuevoEstado,
    Usuario usuarioAdmin
  ) {
    validarUsuarioAdmin(usuarioAdmin);

    if (reporteId == null || nuevoEstado == null) {
      throw new IllegalArgumentException("El reporte y su nuevo estado son obligatorios.");
    }

    ReportePregunta reporte = repositorioRespuestaPartida.buscarReportePorId(reporteId);

    if (reporte == null) {
      throw new IllegalArgumentException("El reporte no existe.");
    }

    reporte.cambiarEstado(nuevoEstado);

    repositorioRespuestaPartida.actualizarReporte(reporte);
  }

  private void validarDatosDeRespuesta(Long partidaId, Long preguntaId, String respuestaElegida) {
    if (
      partidaId == null ||
      preguntaId == null ||
      respuestaElegida == null ||
      respuestaElegida.trim().isEmpty()
    ) {
      throw new IllegalArgumentException("Los datos de la respuesta son inválidos.");
    }
  }

  private Partida obtenerPartida(Long partidaId) {
    Partida partida = repositorioPartida.buscarPorId(partidaId);

    if (partida == null) {
      throw new IllegalArgumentException("La partida no existe.");
    }

    return partida;
  }

  private Pregunta obtenerPregunta(Long preguntaId) {
    Pregunta pregunta = repositorioPregunta.buscarPorId(preguntaId);

    if (pregunta == null) {
      throw new IllegalArgumentException("La pregunta no existe.");
    }

    return pregunta;
  }

  private Jugador obtenerJugadorActual(Partida partida) {
    Jugador jugador = partida.getJugadorEnTurno();

    if (jugador == null) {
      throw new IllegalStateException("La partida no tiene un jugador en turno.");
    }

    return jugador;
  }

  private void guardarNuevaRespuesta(
    Partida partida,
    Pregunta pregunta,
    Jugador jugador,
    String respuestaElegida,
    boolean correcta
  ) {
    RespuestaPartida nuevaRespuesta = new RespuestaPartida(
      partida,
      pregunta,
      jugador,
      jugador.getUsuario(),
      respuestaElegida,
      correcta
    );

    repositorioRespuestaPartida.guardar(nuevaRespuesta);
  }

  private void actualizarRespuestaExistente(
    RespuestaPartida respuestaExistente,
    String respuestaElegida,
    boolean correcta
  ) {
    if (respuestaExistente.coincideCon(respuestaElegida, correcta)) {
      return;
    }

    respuestaExistente.actualizarRespuesta(respuestaElegida, correcta);

    repositorioRespuestaPartida.actualizar(respuestaExistente);
  }

  private void validarDatosDeReporte(
    Long partidaId,
    Long respuestaPartidaId,
    Long usuarioId,
    MotivoReportePregunta motivo
  ) {
    if (partidaId == null || respuestaPartidaId == null || usuarioId == null || motivo == null) {
      throw new IllegalArgumentException("Los datos del reporte son inválidos.");
    }
  }

  private RespuestaPartida obtenerRespuestaPartida(Long respuestaPartidaId) {
    RespuestaPartida respuesta = repositorioRespuestaPartida.buscarPorId(respuestaPartidaId);

    if (respuesta == null) {
      throw new IllegalArgumentException("La respuesta seleccionada no existe.");
    }

    return respuesta;
  }

  private void validarRespuestaDePartida(RespuestaPartida respuesta, Long partidaId) {
    if (!partidaId.equals(respuesta.getPartida().getId())) {
      throw new IllegalStateException("La respuesta no pertenece a esta partida.");
    }
  }

  private void validarPartidaFinalizada(Partida partida) {
    boolean fueAbandonada = partida.getEstadoDePartida() == EstadoDePartida.ABANDONADA;

    if (!partida.estaFinalizada() || fueAbandonada) {
      throw new IllegalStateException(
        "Solo se pueden reportar preguntas de una partida finalizada."
      );
    }
  }

  private void validarPropietarioDeRespuesta(RespuestaPartida respuesta, Long usuarioId) {
    Usuario usuarioRespuesta = respuesta.getUsuario();

    if (usuarioRespuesta == null || !usuarioId.equals(usuarioRespuesta.getId())) {
      throw new IllegalStateException("No podés reportar una respuesta de otro usuario.");
    }
  }

  private void validarReporteInexistente(Long respuestaPartidaId) {
    ReportePregunta reporteExistente = repositorioRespuestaPartida.buscarReportePorRespuestaId(
      respuestaPartidaId
    );

    if (reporteExistente != null) {
      throw new IllegalStateException("Esta pregunta ya fue reportada.");
    }
  }

  private Usuario obtenerUsuario(Long usuarioId) {
    Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);

    if (usuario == null) {
      throw new IllegalArgumentException("El usuario no existe.");
    }

    return usuario;
  }

  private String normalizarComentario(String comentario) {
    if (comentario == null) {
      return "";
    }

    String comentarioNormalizado = comentario.trim();

    if (comentarioNormalizado.length() > LONGITUD_MAXIMA_COMENTARIO) {
      throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres.");
    }

    return comentarioNormalizado;
  }

  private void validarUsuarioAdmin(Usuario usuarioAdmin) {
    boolean esAdmin =
      usuarioAdmin != null &&
      usuarioAdmin.getRol() != null &&
      ROL_ADMIN.equals(usuarioAdmin.getRol().getDescripcion());

    if (!esAdmin) {
      throw new IllegalArgumentException("Solo un administrador puede revisar reportes.");
    }
  }
}
