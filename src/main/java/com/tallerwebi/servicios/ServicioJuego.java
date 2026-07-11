package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Provincia;
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

  void concretarConquista(Long partidaId, Long idProvincia);

  void concretarColonizacion(Long partidaId, Long idProvincia);

  void actualizarPartida(Partida partida);

  Long inicializarPartida(DatosLobby datosLobby, Usuario usuarioAnfitrion);

  void finalizarYRegistrarPartida(Long partidaId, Long usuarioId);

  void avanzarTurno(Long partidaId);

  boolean evaluarYFinalizarPartida(Long partidaId, Long usuarioId);

  void iniciarAtaque(Long partidaId, Long idProvincia)
    throws TiempoAgotadoException, TurnoInvalidoException;

  Set<Long> obtenerPreguntasHechas(Long partidaId);

  void registrarPreguntaHecha(Long partidaId, Long idPregunta);

  String evaluarAcierto(Long partidaId, Long idProvincia, Integer respondidas, Integer requeridas);

  List<Provincia> obtenerProvinciasDelTablero();

  void validarTurnoMultijugador(Long partidaId, Long id) throws TurnoInvalidoException;

  void abandonarPartidaLocal(Long partidaId, Long usuarioId);
}
