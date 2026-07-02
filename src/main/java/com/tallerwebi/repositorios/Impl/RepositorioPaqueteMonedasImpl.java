package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.PaqueteMonedas;
import com.tallerwebi.repositorios.RepositorioPaqueteMonedas;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPaqueteMonedasImpl implements RepositorioPaqueteMonedas {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioPaqueteMonedasImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public PaqueteMonedas buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(PaqueteMonedas.class, id);
  }

  @Override
  public List<PaqueteMonedas> obtenerTodos() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("from PaqueteMonedas", PaqueteMonedas.class)
      .getResultList();
  }
}
