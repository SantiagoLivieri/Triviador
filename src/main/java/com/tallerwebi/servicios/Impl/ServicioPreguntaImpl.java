package com.tallerwebi.servicios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioUsuario;
import java.util.ArrayList;
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
  private final ServicioUsuario servicioUsuario;

  public ServicioPreguntaImpl(
    RepositorioPregunta repositorioPregunta,
    ServicioUsuario servicioUsuario
  ) {
    this.repositorioPregunta = repositorioPregunta;
    this.servicioUsuario = servicioUsuario;
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
        "No se puede mezclar opciones de una pregunta inexistente"
      );
    }
    return pregunta.getOpcionesMezcladas();
  }

  @Override
  public Pregunta buscarPorId(Long id) {
    return repositorioPregunta.buscarPorId(id);
  }

  @Override
  public boolean validarRespuesta(Long idPregunta, String respuesta) {
    Pregunta pregunta = this.buscarPorId(idPregunta);
    if (pregunta == null) {
      throw new IllegalArgumentException("La pregunta con ID " + idPregunta + " no existe.");
    }
    return pregunta.getRespuestaCorrecta().trim().equalsIgnoreCase(respuesta.trim());
  }

  @Override
  public List<String> aplicarComodinEliminarDos(
    Long idUsuario,
    List<String> opcionesEnPantalla,
    Pregunta pregunta
  ) {
    Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);
    if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");

    usuario.consumirComodin("ELIMINAR_2");
    servicioUsuario.actualizarUsuario(usuario);

    String correcta = pregunta.getRespuestaCorrecta();
    List<String> incorrectasEnPantalla = new ArrayList<>();

    for (String opcion : opcionesEnPantalla) {
      if (!opcion.equals(correcta)) {
        incorrectasEnPantalla.add(opcion);
      }
    }

    Collections.shuffle(incorrectasEnPantalla);

    List<String> opcionesSobrevivientes = new ArrayList<>();
    opcionesSobrevivientes.add(correcta);

    if (!incorrectasEnPantalla.isEmpty()) {
      opcionesSobrevivientes.add(incorrectasEnPantalla.get(0));
    }

    Collections.shuffle(opcionesSobrevivientes);
    return opcionesSobrevivientes;
  }

  @Override
  public void aplicarComodinDobleChance(Long idUsuario) {
    Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);
    if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");

    usuario.consumirComodin("DOBLE_CHANCE");
    servicioUsuario.actualizarUsuario(usuario);
  }

  @Override
  public Pregunta aplicarComodinPasarPregunta(
    Long idUsuario,
    Pregunta preguntaActual,
    Long idProvincia,
    Set<Long> preguntasYaHechas
  ) {
    Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);
    if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");

    usuario.consumirComodin("PASAR_PREGUNTA");
    servicioUsuario.actualizarUsuario(usuario);

    if (preguntaActual != null) {
      preguntasYaHechas.add(preguntaActual.getId());
    }

    return this.obtenerPreguntaPorProvincia(idProvincia, preguntasYaHechas);
  }

  @Override
  public List<String> removerOpcionIncorrecta(List<String> opciones, String respuestaIncorrecta) {
    if (opciones != null) {
      opciones.remove(respuestaIncorrecta);
    }
    return opciones;
  }
}
