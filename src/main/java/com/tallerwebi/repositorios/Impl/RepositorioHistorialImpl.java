package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.repositorios.RepositorioHistorial;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioHistorial")
public class RepositorioHistorialImpl implements RepositorioHistorial {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioHistorialImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(HistorialPartida historial) {
    sessionFactory.getCurrentSession().save(historial);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<HistorialPartida> buscarPorUsuarioId(Long usuarioId) {
    return sessionFactory
      .getCurrentSession()
      .createQuery(
        "FROM HistorialPartida WHERE usuario.id = :usuarioId ORDER BY fechaFinalizacion DESC"
      )
      .setParameter("usuarioId", usuarioId)
      .list();
  }
}
