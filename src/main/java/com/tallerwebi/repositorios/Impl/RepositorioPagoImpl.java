package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.PagoProcesado;
import com.tallerwebi.repositorios.RepositorioPago;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPagoImpl implements RepositorioPago {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioPagoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public PagoProcesado buscarPagoPorId(Long idMercadoPago) {
    return sessionFactory.getCurrentSession().get(PagoProcesado.class, idMercadoPago);
  }

  @Override
  public void guardarPago(PagoProcesado pagoProcesado) {
    sessionFactory.getCurrentSession().save(pagoProcesado);
  }
}
