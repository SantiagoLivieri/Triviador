package com.tallerwebi.servicios;

import com.tallerwebi.entidades.PaqueteMonedas;

public interface ServicioPago {
  String crearPreferenciaDePago(PaqueteMonedas paquete, Long idUsuario);

  void procesarPagoAprobado(Long idPago);
}
