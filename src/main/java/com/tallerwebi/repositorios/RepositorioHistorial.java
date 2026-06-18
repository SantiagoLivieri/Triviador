package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.HistorialPartida;
import java.util.List;

public interface RepositorioHistorial {
  void guardar(HistorialPartida historial);
  List<HistorialPartida> buscarPorUsuarioId(Long usuarioId);
}
