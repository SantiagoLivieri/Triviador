package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Rol;
import com.tallerwebi.repositorios.RepositorioRol;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioRol")
public class RepositorioRolImpl implements RepositorioRol {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioRolImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Rol buscarRolPorId(Long id) {
    return (Rol) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Rol WHERE id = :id")
      .setParameter("id", id)
      .uniqueResult();
  }

  @Override
  public Rol buscarPorDescripcion(String descripcion) {
    return (Rol) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Rol WHERE descripcion = :descripcion")
      .setParameter("descripcion", descripcion)
      .uniqueResult();
  }

  @Override
  public void guardar(Rol rol) {
    sessionFactory.getCurrentSession().save(rol);
  }
}
