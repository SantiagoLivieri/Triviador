package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import java.util.List;

public interface ServicioJuego {
  void inicializarPartida(DatosLobby datosLobby);

  List<Jugador> obtenerJugadores();

  List<Provincia> obtenerProvincias();

  Jugador obtenerJugadorDelTurno(Integer turnoActual);

  Pregunta obtenerPreguntaAleatoria();

  Boolean responderPregunta(
    Long idProvincia,
    Long idPregunta,
    String respuesta,
    Integer turnoActual
  );
}
