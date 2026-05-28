package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.util.List;

public interface ServicioJuego {
  Long inicializarPartida(DatosLobby datosLobby);

  Partida obtenerPartidaPorId(Long partidaId);

  List<Provincia> obtenerProvincias();

  Pregunta obtenerPreguntaAleatoria();

  List<String> obtenerOpcionesMezcladas(Pregunta pregunta);

  void procesarJugada(Long partidaId, Long jugadorId, Long provinciaSeleccionadaId)
    throws TiempoAgotadoException, TurnoInvalidoException;

  Boolean procesarRespuestaYPasarTurno(
    Long partidaId,
    Long idProvincia,
    Long idPregunta,
    String respuesta
  );

  void forzarSaltoPorTiempo(Long partidaId);

  List<Jugador> obtenerJugadores();
}
