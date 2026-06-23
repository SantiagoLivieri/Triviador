package com.tallerwebi.repositorios;

import com.tallerwebi.repositorios.Impl.RepositorioPreguntaImpl;
import com.tallerwebi.repositorios.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioPreguntaTest {

  @Autowired
  private SessionFactory sessionFactory;

  @SuppressWarnings("unused")
  private RepositorioPregunta repositorioPregunta;

  @BeforeEach
  public void init() {
    this.repositorioPregunta = new RepositorioPreguntaImpl(sessionFactory);
  }

  @Test
  public void queSePuedaGuardarUnaPregunta() {}
}
