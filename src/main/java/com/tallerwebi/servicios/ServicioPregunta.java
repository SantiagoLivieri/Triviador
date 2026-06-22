package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Pregunta;
import java.util.List;
import java.util.Set;

public interface ServicioPregunta {
  List<String> obtenerOpcionesMezcladas(Pregunta pregunta);

  Pregunta buscarPorId(Long id);

  Pregunta obtenerPreguntaPorProvincia(Long idProvincia, Set<Long> preguntasYaHechas);

  boolean validarRespuesta(Long idPregunta, String respuesta);

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

  List<String> removerOpcionIncorrecta(List<String> opciones, String respuestaIncorrecta);
}
