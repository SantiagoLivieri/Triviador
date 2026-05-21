package com.tallerwebi.servicios;

import com.tallerwebi.entidades.CategoriaPregunta;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.TipoPregunta;
import com.tallerwebi.repositorios.RepositorioPregunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioCargaInicialImpl implements ServicioCargaInicial {

  private static final Long CANTIDAD_MINIMA_PREGUNTAS = 10L;
  private static final String BUENOS_AIRES = "Buenos Aires";
  private static final String MENDOZA = "Mendoza";

  private final RepositorioPregunta repositorioPregunta;

  @Autowired
  public ServicioCargaInicialImpl(RepositorioPregunta repositorioPregunta) {
    this.repositorioPregunta = repositorioPregunta;
  }

  @Override
  public void cargarPreguntasIniciales() {
    if (repositorioPregunta.contar() >= CANTIDAD_MINIMA_PREGUNTAS) {
      return;
    }

    repositorioPregunta.guardar(
      crearPregunta("Cual es la capital de Argentina?", BUENOS_AIRES, "Cordoba", "Rosario", MENDOZA)
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "Que provincia argentina es conocida por el vino Malbec?",
        MENDOZA,
        "Chaco",
        "Formosa",
        "Santa Cruz"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "En que provincia esta la ciudad de Rosario?",
        "Santa Fe",
        "Cordoba",
        BUENOS_AIRES,
        "San Juan"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "Cual es la provincia mas austral de Argentina?",
        "Tierra del Fuego",
        "Santa Cruz",
        "Chubut",
        "Rio Negro"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "En que provincia se encuentra Mar del Plata?",
        BUENOS_AIRES,
        "Santa Fe",
        "Entre Rios",
        "La Pampa"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "Que provincia limita con Chile y es famosa por el Aconcagua?",
        MENDOZA,
        "Misiones",
        "Corrientes",
        "Chaco"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "En que provincia estan las Cataratas del Iguazu?",
        "Misiones",
        "Salta",
        "Jujuy",
        "Formosa"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "Cual de estas provincias pertenece a la Patagonia?",
        "Chubut",
        "Tucuman",
        "Santa Fe",
        "Chaco"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "Que provincia argentina tiene como capital a La Plata?",
        BUENOS_AIRES,
        "Cordoba",
        MENDOZA,
        "San Luis"
      )
    );

    repositorioPregunta.guardar(
      crearPregunta(
        "En que provincia se encuentra la Quebrada de Humahuaca?",
        "Jujuy",
        "Salta",
        "Catamarca",
        "La Rioja"
      )
    );
  }

  private Pregunta crearPregunta(
    String enunciado,
    String respuestaCorrecta,
    String opcionIncorrectaUno,
    String opcionIncorrectaDos,
    String opcionIncorrectaTres
  ) {
    return new Pregunta(
      enunciado,
      respuestaCorrecta,
      opcionIncorrectaUno,
      opcionIncorrectaDos,
      opcionIncorrectaTres,
      TipoPregunta.MULTIPLE_CHOICE,
      CategoriaPregunta.GEOGRAFIA
    );
  }
}
