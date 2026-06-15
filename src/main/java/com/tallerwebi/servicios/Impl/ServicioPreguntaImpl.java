package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioPregunta;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioPreguntaImpl implements ServicioPregunta {

  private final RepositorioPregunta repositorioPregunta;

  public ServicioPreguntaImpl(RepositorioPregunta repositorioPregunta) {
    this.repositorioPregunta = repositorioPregunta;
  }

  @Override
  public Pregunta obtenerPreguntaPorProvincia(Long idProvincia) {
    List<Pregunta> preguntasFiltradas = repositorioPregunta.buscarPorProvincia(idProvincia);
    if (preguntasFiltradas.isEmpty()) {
      List<Pregunta> todasLasPreguntas = repositorioPregunta.buscarTodas();
      if (todasLasPreguntas.isEmpty()) {
        throw new IllegalArgumentException("No se encontraron preguntas existentes");
      }
      Collections.shuffle(todasLasPreguntas);
      return todasLasPreguntas.get(0);
    }
    Collections.shuffle(preguntasFiltradas);
    return preguntasFiltradas.get(0);
  }

  @Override
  public List<String> obtenerOpcionesMezcladas(Pregunta pregunta) {
    if (pregunta == null) {
      throw new IllegalArgumentException(
        "No se puede mezclar opciones de una pregunta inexistente"
      );
    }
    return pregunta.getOpcionesMezcladas();
  }

  @Override
  public Pregunta buscarPorId(Long id) {
    return repositorioPregunta.buscarPorId(id);
  }
}
