package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Pregunta;
import java.util.List;

public interface ServicioPregunta {
  Pregunta obtenerPreguntaPorProvincia(Long idProvincia);
  List<String> obtenerOpcionesMezcladas(Pregunta pregunta);
  Pregunta buscarPorId(Long id);
}
