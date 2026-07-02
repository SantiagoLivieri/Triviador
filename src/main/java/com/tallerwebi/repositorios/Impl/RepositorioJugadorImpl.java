package com.tallerwebi.repositorios.Impl;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.repositorios.RepositorioJugador;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementacion del repositorio de jugadores.
 */
@Repository
@Transactional
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

  @Override
  public Jugador buscarPorUsuarioIdYPartidaId(Long usuarioId, Long partidaId) {
    return (Jugador) sessionFactory
      .getCurrentSession()
      .createQuery("from Jugador where usuario.id = :usuarioId and partida.id = :partidaId")
      .setParameter("usuarioId", usuarioId)
      .setParameter("partidaId", partidaId)
      .uniqueResult();
  }
}
