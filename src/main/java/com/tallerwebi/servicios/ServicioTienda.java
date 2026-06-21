package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Comodin;
import java.util.List;

public interface ServicioTienda {
  void procesarCompraDeComodin(Long idUsuario, String nombreComodin);
  List<Comodin> obtenerCatalogoDeComodines();
}
