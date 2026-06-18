package com.tallerwebi.servicios;

import com.tallerwebi.entidades.HistorialPartida;
import java.util.List;

public interface ServicioHistorial {
  List<HistorialPartida> buscarHistorialPorUsuario(Long usuarioId);

  void guardar(HistorialPartida historial);
}
