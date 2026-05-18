package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Usuario;
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
  public void crearUsuario(Usuario usuario) {
    sessionFactory.getCurrentSession().save(usuario);
  }
}
