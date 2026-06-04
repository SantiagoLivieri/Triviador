package com.tallerwebi.servicios;

import com.tallerwebi.servicios.excepcion.CargaPreguntasException;

public interface ServicioCargaPreguntas {
  void ejecutarCargaInicial() throws CargaPreguntasException;
}
