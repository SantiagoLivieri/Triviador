package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import java.util.List;
import java.util.Set;

public interface ServicioEntrenamiento {
  List<Provincia> obtenerProvinciasConPreguntas();

  Pregunta obtenerPreguntaParaEntrenamiento(Long idProvincia, Set<Long> usadas);
}
