package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.repositorios.RepositorioComodin;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioComodin")
public class RepositorioComodinImpl implements RepositorioComodin {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioComodinImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Comodin buscarPorNombre(String nombre) {
    return (Comodin) sessionFactory
      .getCurrentSession()
      .createCriteria(Comodin.class)
      .add(Restrictions.eq("nombre", nombre))
      .uniqueResult();
  }

  @Override
  public List<Comodin> buscarTodos() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("from Comodin c order by c.nombre", Comodin.class)
      .getResultList();
  }
}
