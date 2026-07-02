package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.PaqueteMonedas;
import java.util.List;

public interface RepositorioPaqueteMonedas {
  PaqueteMonedas buscarPorId(Long id);

  List<PaqueteMonedas> obtenerTodos();
}
