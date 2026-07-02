package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.EstadoSugerenciaPregunta;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.repositorios.RepositorioSugerenciaPregunta;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioSugerenciaPreguntaImpl implements RepositorioSugerenciaPregunta {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioSugerenciaPreguntaImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(SugerenciaPregunta sugerencia) {
    sessionFactory.getCurrentSession().save(sugerencia);
  }

  @Override
  public void actualizar(SugerenciaPregunta sugerencia) {
    sessionFactory.getCurrentSession().update(sugerencia);
  }

  @Override
  public void eliminar(SugerenciaPregunta sugerencia) {
    sessionFactory.getCurrentSession().delete(sugerencia);
  }

  @Override
  public SugerenciaPregunta buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(SugerenciaPregunta.class, id);
  }

  @Override
  public List<SugerenciaPregunta> buscarPorEstado(EstadoSugerenciaPregunta estado) {
    return sessionFactory
      .getCurrentSession()
      .createQuery(
        "FROM SugerenciaPregunta s WHERE s.estado = :estado ORDER BY s.fechaCreacion DESC",
        SugerenciaPregunta.class
      )
      .setParameter("estado", estado)
      .getResultList();
  }

  @Override
  public List<SugerenciaPregunta> buscarTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery(
        "FROM SugerenciaPregunta s ORDER BY s.fechaCreacion DESC",
        SugerenciaPregunta.class
      )
      .getResultList();
  }
}
