package com.tallerwebi.repositorios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tallerwebi.entidades.Partida;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestConfig.class })
public class RepositorioPartidaTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioPartida repositorioPartida;

  @BeforeEach
  public void init() {
    this.repositorioPartida = new RepositorioPartidaImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void queSePuedaGuardarUnaPartidaYBuscarlaPorSuId() {
    Partida nuevaPartida = new Partida();

    nuevaPartida.setEtapaActual(1);

    this.repositorioPartida.guardar(nuevaPartida);

    Long idGenerado = nuevaPartida.getId();

    Partida partidaBuscada = this.repositorioPartida.buscarPorId(idGenerado);

    assertNotNull(partidaBuscada);
    assertThat(partidaBuscada.getId(), is(equalTo(idGenerado)));
  }

  @Test
  @Transactional
  @Rollback
  public void queAlBuscarUnaPartidaInexistenteDevuelvaNull() {
    Long idInexistente = 3892L;

    Partida partidaBuscada = this.repositorioPartida.buscarPorId(idInexistente);

    assertThat(partidaBuscada, is(nullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void queSePuedaActualizarLaEtapaDeUnaPartida() {
    Partida partida = new Partida();
    partida.setEtapaActual(1);
    this.repositorioPartida.guardar(partida);

    partida.setEtapaActual(2);
    this.repositorioPartida.actualizar(partida);

    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();

    Partida partidaRecuperada = this.repositorioPartida.buscarPorId(partida.getId());

    assertNotNull(partidaRecuperada);
    assertThat(partidaRecuperada.getEtapaActual(), is(2));
  }
}
