package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Pregunta;
import org.hibernate.SessionFactory;

public class RepositorioPreguntaImpl implements RepositorioPregunta {

  private SessionFactory sessionFactory;

  @Override
  public void guardarPregunta(Pregunta pregunta) {
    sessionFactory.getCurrentSession().save(pregunta);
  }
}
