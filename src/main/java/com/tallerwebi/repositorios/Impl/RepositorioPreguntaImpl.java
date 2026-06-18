package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.repositorios.RepositorioPregunta;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPreguntaImpl implements RepositorioPregunta {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioPreguntaImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Pregunta pregunta) {
    sessionFactory.getCurrentSession().save(pregunta);
  }

  @Override
  public Pregunta buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(Pregunta.class, id);
  }

  @Override
  public List<Pregunta> buscarTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("from Pregunta p order by p.id", Pregunta.class)
      .getResultList();
  }

  @Override
  public Long contar() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("select count(p) from Pregunta p", Long.class)
      .getSingleResult();
  }

  @Override
  public List<Pregunta> buscarPorProvincia(Long idProvincia) {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM Pregunta p WHERE p.provincia.id = :idProvincia", Pregunta.class)
      .setParameter("idProvincia", idProvincia)
      .getResultList();
  }
}
