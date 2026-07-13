package com.tallerwebi.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.repositorios.Impl.RepositorioPreguntaImpl;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepositorioPreguntaTest {

  private SessionFactory sessionFactory;
  private Session session;
  private RepositorioPreguntaImpl repositorioPregunta;

  @BeforeEach
  public void init() {
    sessionFactory = mock(SessionFactory.class);

    session = mock(Session.class);

    when(sessionFactory.getCurrentSession()).thenReturn(session);

    repositorioPregunta = new RepositorioPreguntaImpl(sessionFactory);
  }

  @Test
  public void alGuardarPreguntaDebeUsarLaSesionActualYGuardarLaEntidad() {
    Pregunta pregunta = mock(Pregunta.class);

    repositorioPregunta.guardar(pregunta);

    verify(sessionFactory).getCurrentSession();

    verify(session).save(pregunta);
  }

  @Test
  public void alBuscarPreguntaPorIdDebeUsarLaSesionActualYRetornarLaPregunta() {
    Pregunta preguntaEsperada = mock(Pregunta.class);

    when(session.get(Pregunta.class, 1L)).thenReturn(preguntaEsperada);

    Pregunta resultado = repositorioPregunta.buscarPorId(1L);

    assertEquals(preguntaEsperada, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).get(Pregunta.class, 1L);
  }

  @Test
  public void alBuscarTodasDebeCrearQueryOrdenadaPorIdYRetornarLista() {
    Pregunta pregunta = mock(Pregunta.class);
    List<Pregunta> preguntasEsperadas = List.of(pregunta);

    Query<Pregunta> query = crearQueryMock();

    String hql = "from Pregunta p order by p.id";

    when(session.createQuery(hql, Pregunta.class)).thenReturn(query);

    when(query.getResultList()).thenReturn(preguntasEsperadas);

    List<Pregunta> resultado = repositorioPregunta.buscarTodas();

    assertEquals(preguntasEsperadas, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(hql, Pregunta.class);
    verify(query).getResultList();
  }

  @Test
  public void alContarPreguntasDebeCrearQueryDeCountYRetornarCantidad() {
    Query<Long> query = crearQueryMock();

    String hql = "select count(p) from Pregunta p";

    when(session.createQuery(hql, Long.class)).thenReturn(query);

    when(query.getSingleResult()).thenReturn(15L);

    Long resultado = repositorioPregunta.contar();

    assertEquals(15L, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(hql, Long.class);
    verify(query).getSingleResult();
  }

  @Test
  public void alBuscarPorProvinciaDebeCrearQueryConParametroYRetornarLista() {
    Long idProvincia = 3L;

    Pregunta pregunta = mock(Pregunta.class);
    List<Pregunta> preguntasEsperadas = List.of(pregunta);

    Query<Pregunta> query = crearQueryMock();

    String hql = "FROM Pregunta p WHERE p.provincia.id = :idProvincia";

    when(session.createQuery(hql, Pregunta.class)).thenReturn(query);

    when(query.setParameter("idProvincia", idProvincia)).thenReturn(query);

    when(query.getResultList()).thenReturn(preguntasEsperadas);

    List<Pregunta> resultado = repositorioPregunta.buscarPorProvincia(idProvincia);

    assertEquals(preguntasEsperadas, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(hql, Pregunta.class);
    verify(query).setParameter("idProvincia", idProvincia);
    verify(query).getResultList();
  }

  @SuppressWarnings("unchecked")
  private <T> Query<T> crearQueryMock() {
    return mock(Query.class);
  }
}
