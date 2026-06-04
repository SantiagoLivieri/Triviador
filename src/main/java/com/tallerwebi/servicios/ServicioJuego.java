package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;

public interface ServicioJuego {
  Long inicializarPartida(DatosLobby datosLobby);

  Partida obtenerPartidaPorId(Long partidaId);

  void procesarJugada(Long partidaId, Long jugadorId, Long provinciaSeleccionadaId)
    throws TiempoAgotadoException, TurnoInvalidoException;

  Boolean procesarRespuestaYPasarTurno(
    Long partidaId,
    Long idProvincia,
    Long idPregunta,
    String respuesta
  );

  void forzarSaltoPorTiempo(Long partidaId);

  void concretarConquista(Long partidaId, Long idProvincia);
}
