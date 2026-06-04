package com.tallerwebi.servicios.Impl;

import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.ServicioCargaPreguntas;
import com.tallerwebi.servicios.excepcion.CargaPreguntasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioCargaPreguntas")
@Transactional
public class ServicioCargaPreguntasImpl implements ServicioCargaPreguntas {

  private final RepositorioPregunta repositorioPregunta;
  private static final int CANTIDAD_MINIMA_PREGUNTAS = 15;

  @Autowired
  public ServicioCargaPreguntasImpl(RepositorioPregunta repositorioPregunta) {
    this.repositorioPregunta = repositorioPregunta;
  }

  @Override
  public void ejecutarCargaInicial() throws CargaPreguntasException {
    long cantidadActual = repositorioPregunta.contar();

    if (cantidadActual < CANTIDAD_MINIMA_PREGUNTAS) {
      throw new CargaPreguntasException(
        "El lote inicial de preguntas desde preguntas.sql falló o está incompleto. " +
        "Cantidad encontrada: " +
        cantidadActual
      );
    }
  }
}
