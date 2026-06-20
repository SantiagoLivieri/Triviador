package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.util.List;
import java.util.Set;

public interface ServicioJuego {
  Partida obtenerPartidaPorId(Long partidaId);

  Usuario obtenerUsuarioPorId(Long usuarioId);

  void procesarJugada(Long partidaId, Long jugadorId, Long provinciaSeleccionadaId)
    throws TiempoAgotadoException, TurnoInvalidoException;

  void forzarSaltoPorTiempo(Long partidaId);

  Integer obtenerCantidadPreguntasRequeridas(Long idProvincia);

  void validarAtaque(Long jugadorId, Long idProvincia);

  boolean disputaFinalizada(Integer respondidas, Integer requeridas);

  boolean esConquista(Integer preguntasrequeridas);

  void concretarConquista(Long partidaId, Long idProvincia);

  void concretarColonizacion(Long partidaId, Long idProvincia);

  void actualizarPartida(Partida partida);

  Long inicializarPartida(DatosLobby datosLobby, Usuario usuarioAnfitrion);

  void finalizarYRegistrarPartida(Long partidaId, Long usuarioId);

  List<String> aplicarComodinEliminarDos(
    Long idUsuario,
    List<String> opcionesEnPantalla,
    Pregunta pregunta
  );

  void aplicarComodinDobleChance(Long idUsuario);

  Pregunta aplicarComodinPasarPregunta(
    Long idUsuario,
    Pregunta preguntaActual,
    Long idProvincia,
    Set<Long> preguntasYaHechas
  );

  Boolean procesarRespuestaYPasarTurno(
    Long partidaId,
    Long idProvincia,
    Long idPregunta,
    String respuesta,
    Boolean dobleChance
  );
}
