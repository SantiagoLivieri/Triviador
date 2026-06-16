package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioPregunta;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
  public Pregunta obtenerPreguntaPorProvincia(Long idProvincia, Set<Long> preguntasYaHechas) {
    List<Pregunta> preguntasFiltradas = repositorioPregunta.buscarPorProvincia(idProvincia);
    List<Pregunta> disponiblesProvincia = preguntasFiltradas
        .stream()
        .filter(p -> !preguntasYaHechas.contains(p.getId()))
        .collect(Collectors.toList());

    if (!disponiblesProvincia.isEmpty()) {
      Collections.shuffle(disponiblesProvincia);
      return disponiblesProvincia.get(0);
    }

    List<Pregunta> todasLasPreguntas = repositorioPregunta.buscarTodas();
    if (todasLasPreguntas.isEmpty()) {
      throw new IllegalArgumentException("No se encontraron preguntas existentes");
    }

    List<Pregunta> disponiblesGeneral = todasLasPreguntas
        .stream()
        .filter(p -> !preguntasYaHechas.contains(p.getId()))
        .collect(Collectors.toList());

    if (!disponiblesGeneral.isEmpty()) {
      Collections.shuffle(disponiblesGeneral);
      return disponiblesGeneral.get(0);
    }

    // en caso de que ya no hayan mas preguntas en la DB (mejor evitar)
    preguntasYaHechas.clear();
    Collections.shuffle(todasLasPreguntas);
    return todasLasPreguntas.get(0);
  }

  @Override
  public List<String> obtenerOpcionesMezcladas(Pregunta pregunta) {
    if (pregunta == null) {
      throw new IllegalArgumentException(
          "No se puede mezclar opciones de una pregunta inexistente");
    }
    return pregunta.getOpcionesMezcladas();
  }

  @Override
  public Pregunta buscarPorId(Long id) {
    return repositorioPregunta.buscarPorId(id);
  }
}
