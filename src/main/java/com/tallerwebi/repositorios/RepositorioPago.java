package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.PagoProcesado;

public interface RepositorioPago {
  PagoProcesado buscarPagoPorId(Long idMercadoPago);
  void guardarPago(PagoProcesado pagoProcesado);
}
