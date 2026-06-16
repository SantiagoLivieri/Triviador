package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Pregunta;
import java.util.List;
import java.util.Set;

public interface ServicioPregunta {
  List<String> obtenerOpcionesMezcladas(Pregunta pregunta);
  Pregunta buscarPorId(Long id);
  Pregunta obtenerPreguntaPorProvincia(Long idProvincia, Set<Long> preguntasYaHechas);
}
