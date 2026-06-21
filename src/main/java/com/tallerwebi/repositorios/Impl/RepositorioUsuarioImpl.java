package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioUsuario;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Usuario buscarUsuario(String email, String password) {
    return (Usuario) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Usuario WHERE email = :email AND password = :password")
      .setParameter("email", email)
      .setParameter("password", password)
      .uniqueResult();
  }

  @Override
  public Usuario buscarUsuarioPorEmail(String email) {
    return (Usuario) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Usuario WHERE email= :email")
      .setParameter("email", email)
      .uniqueResult();
  }

  @Override
  public Usuario buscarUsuarioPorId(Long id) {
    return (Usuario) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Usuario WHERE id = :id")
      .setParameter("id", id)
      .uniqueResult();
  }

  @Override
  public void crearUsuario(Usuario usuario) {
    sessionFactory.getCurrentSession().save(usuario);
  }

  @Override
  public void actualizarUsuario(Usuario usuario) {
    sessionFactory.getCurrentSession().update(usuario);
  }

  @Override
  public List<Usuario> obtenerTop10Historico() {
    final String hql =
      "SELECT u FROM Usuario u " + "ORDER BY u.estadisticas.experiencia DESC, u.nombreJugador ASC";

    return sessionFactory
      .getCurrentSession()
      .createQuery(hql, Usuario.class)
      .setMaxResults(10)
      .list();
  }

  @Override
  public Long obtenerPosicionEnRanking(Integer experiencia, String nombre) {
    final String hql =
      "SELECT COUNT(u) FROM Usuario u " +
      "WHERE u.estadisticas.experiencia > :experienciaActual " +
      "OR (u.estadisticas.experiencia = :experienciaActual AND u.nombreJugador < :nombreActual)";

    return (Long) sessionFactory
      .getCurrentSession()
      .createQuery(hql)
      .setParameter("experienciaActual", experiencia != null ? experiencia : 0)
      .setParameter("nombreActual", nombre != null ? nombre : "")
      .uniqueResult();
  }
}
