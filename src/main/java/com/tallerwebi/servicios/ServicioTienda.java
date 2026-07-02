package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.PaqueteMonedas;
import java.util.List;

public interface ServicioTienda {
  void procesarCompraDeComodin(Long idUsuario, String nombreComodin);
  List<Comodin> obtenerCatalogoDeComodines();
  PaqueteMonedas buscarPaquetePorId(Long idPaquete);
  List<PaqueteMonedas> obtenerTodosLosPaquetes();
}
