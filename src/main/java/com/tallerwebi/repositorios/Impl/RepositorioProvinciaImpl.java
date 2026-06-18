package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.repositorios.RepositorioProvincia;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioProvinciaImpl implements RepositorioProvincia {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioProvinciaImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Provincia provincia) {
    sessionFactory.getCurrentSession().save(provincia);
  }

  @Override
  public void resetearProvincias() {
    sessionFactory
      .getCurrentSession()
      .createQuery("update Provincia p set p.idJugadorDuenio = null, p.puntos = 0")
      .executeUpdate();
  }

  @Override
  public List<Provincia> buscarTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("from Provincia p order by p.nombre", Provincia.class)
      .getResultList();
  }

  @Override
  public Long contar() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("select count(p) from Provincia p", Long.class)
      .getSingleResult();
  }

  @Override
  public void actualizar(Provincia provincia) {
    sessionFactory.getCurrentSession().update(provincia);
  }

  @Override
  public Provincia buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(Provincia.class, id);
  }
}
