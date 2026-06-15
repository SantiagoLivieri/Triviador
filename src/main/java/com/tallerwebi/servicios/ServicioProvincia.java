package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Provincia;
import java.util.List;

public interface ServicioProvincia {
  Provincia buscarPorId(Long idProvincia);
  List<Provincia> obtenerProvincias();
  void resetearProvincias();
  void actualizar(Provincia provincia);
  Integer obtenerCantidadPreguntasRequeridas(Long idProvincia);
}
