package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Provincia;
import java.util.List;

public interface RepositorioProvincia {
  void guardar(Provincia provincia);

  void actualizar(Provincia provincia);

  Provincia buscarPorId(Long id);

  void resetearProvincias();

  List<Provincia> buscarTodas();

  Long contar();
}
