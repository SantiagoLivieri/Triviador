package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Jugador;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementacion del repositorio de jugadores.
 */
@Repository
public class RepositorioJugadorImpl implements RepositorioJugador {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioJugadorImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Jugador jugador) {
    sessionFactory.getCurrentSession().save(jugador);
  }

  @Override
  public void eliminarTodos() {
    sessionFactory.getCurrentSession().createQuery("delete from Jugador").executeUpdate();
  }

  @Override
  public List<Jugador> buscarTodos() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("from Jugador j order by j.id", Jugador.class)
      .getResultList();
  }

  @Override
  public void actualizar(Jugador jugador) {
    sessionFactory.getCurrentSession().update(jugador);
  }

  @Override
  public Jugador buscarPorId(Long idJugadorDuenio) {
    return (Jugador) sessionFactory
      .getCurrentSession()
      .createQuery("from Jugador where id = :id")
      .setParameter("id", idJugadorDuenio)
      .uniqueResult();
  }
}
