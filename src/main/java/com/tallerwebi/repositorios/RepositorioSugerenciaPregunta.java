package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.EstadoSugerenciaPregunta;
import com.tallerwebi.entidades.SugerenciaPregunta;
import java.util.List;

public interface RepositorioSugerenciaPregunta {
  void guardar(SugerenciaPregunta sugerencia);

  void actualizar(SugerenciaPregunta sugerencia);

  void eliminar(SugerenciaPregunta sugerencia);

  SugerenciaPregunta buscarPorId(Long id);

  List<SugerenciaPregunta> buscarPorEstado(EstadoSugerenciaPregunta estado);

  List<SugerenciaPregunta> buscarTodas();
}
