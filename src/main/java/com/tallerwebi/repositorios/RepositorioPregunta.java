package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Pregunta;
import java.util.List;

public interface RepositorioPregunta {
  void guardar(Pregunta pregunta);

  Pregunta buscarPorId(Long id);

  List<Pregunta> buscarTodas();

  Long contar();

  List<Pregunta> buscarPorProvincia(Long idProvincia);
}
