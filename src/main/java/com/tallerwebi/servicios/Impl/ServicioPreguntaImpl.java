package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioPregunta;
import java.util.ArrayList;
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
    List<Pregunta> preguntas = repositorioPregunta.buscarTodas();
    List<Pregunta> preguntasFiltradas = new ArrayList<>();
    for (Pregunta pregunta : preguntas) {
      if (pregunta.getProvincia() != null && pregunta.getProvincia().getId().equals(idProvincia)) {
        preguntasFiltradas.add(pregunta);
      }
    }
    if (preguntasFiltradas.isEmpty()) {
      if (preguntas.isEmpty()) {
        return null;
      }
      Collections.shuffle(preguntas);
      return preguntas.get(0);
    }
    Collections.shuffle(preguntasFiltradas);
    return preguntasFiltradas.get(0);
  }

  @Override
  public List<String> obtenerOpcionesMezcladas(Pregunta pregunta) {
    List<String> opciones = new ArrayList<>();
    opciones.add(pregunta.getRespuestaCorrecta());
    opciones.add(pregunta.getOpcionIncorrectaUno());
    opciones.add(pregunta.getOpcionIncorrectaDos());
    opciones.add(pregunta.getOpcionIncorrectaTres());
    Collections.shuffle(opciones);
    return opciones;
  }

  @Override
  public Pregunta buscarPorId(Long id) {
    return repositorioPregunta.buscarPorId(id);
  }
}
