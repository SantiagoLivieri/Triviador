package com.tallerwebi.repositorios;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.entidades.Partida;

@Repository("repositorioPartida")
public class RepositorioPartidaImpl implements RepositorioPartida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Partida partida) {
        sessionFactory.getCurrentSession().save(partida);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Partida buscarPorId(Long id) {
        Partida partida = (Partida) sessionFactory.getCurrentSession()
                .createCriteria(Partida.class)
                .add(org.hibernate.criterion.Restrictions.eq("id", id))
                .uniqueResult();
                
        if (partida != null && partida.getJugadores() != null) {
            Hibernate.initialize(partida.getJugadores());
        }

        return partida;
    }

    @Override
    public void actualizar(Partida partida) {
        sessionFactory.getCurrentSession().update(partida);
    }
}
