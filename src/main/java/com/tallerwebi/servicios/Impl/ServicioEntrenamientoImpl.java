package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioEntrenamiento;
import com.tallerwebi.servicios.ServicioProvincia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioEntrenamientoImpl implements ServicioEntrenamiento {

  private final ServicioProvincia servicioProvincia;
  private final RepositorioPregunta repositorioPregunta;

  public ServicioEntrenamientoImpl(
    ServicioProvincia servicioProvincia,
    RepositorioPregunta repositorioPregunta
  ) {
    this.servicioProvincia = servicioProvincia;
    this.repositorioPregunta = repositorioPregunta;
  }

  @Override
  public List<Provincia> obtenerProvinciasConPreguntas() {
    return servicioProvincia
      .obtenerProvincias()
      .stream()
      .filter(provincia -> !repositorioPregunta.buscarPorProvincia(provincia.getId()).isEmpty())
      .collect(Collectors.toList());
  }

  @Override
  public Pregunta obtenerPreguntaParaEntrenamiento(Long idProvincia, Set<Long> usadas) {
    if (idProvincia != null) {
      List<Pregunta> preguntasProvincia = repositorioPregunta.buscarPorProvincia(idProvincia);
      return obtenerPreguntaNoUsada(preguntasProvincia, usadas);
    }

    List<Provincia> provincias = obtenerProvinciasConPreguntas();
    if (provincias.isEmpty()) {
      return null;
    }

    List<Provincia> provinciasAleatorias = new ArrayList<>(provincias);
    Collections.shuffle(provinciasAleatorias);

    for (Provincia provincia : provinciasAleatorias) {
      Pregunta pregunta = obtenerPreguntaNoUsada(
        repositorioPregunta.buscarPorProvincia(provincia.getId()),
        usadas
      );
      if (pregunta != null) {
        return pregunta;
      }
    }

    return null;
  }

  private Pregunta obtenerPreguntaNoUsada(List<Pregunta> preguntas, Set<Long> usadas) {
    if (preguntas == null || preguntas.isEmpty()) {
      return null;
    }

    List<Pregunta> disponibles = new ArrayList<>(preguntas);
    disponibles.removeIf(pregunta -> usadas.contains(pregunta.getId()));

    if (disponibles.isEmpty()) {
      return null;
    }

    Collections.shuffle(disponibles);
    return disponibles.get(0);
  }
}
