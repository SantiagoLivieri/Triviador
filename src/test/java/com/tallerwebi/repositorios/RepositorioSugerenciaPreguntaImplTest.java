package com.tallerwebi.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.EstadoSugerenciaPregunta;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.repositorios.Impl.RepositorioSugerenciaPreguntaImpl;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepositorioSugerenciaPreguntaImplTest {

  private SessionFactory sessionFactory;
  private Session session;
  private RepositorioSugerenciaPreguntaImpl repositorio;

  @BeforeEach
  public void init() {
    sessionFactory = mock(SessionFactory.class);

    session = mock(Session.class);

    when(sessionFactory.getCurrentSession()).thenReturn(session);

    repositorio = new RepositorioSugerenciaPreguntaImpl(sessionFactory);
  }

  @Test
  public void alGuardarSugerenciaDebeUsarLaSesionActualYGuardarLaEntidad() {
    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    repositorio.guardar(sugerencia);

    verify(sessionFactory).getCurrentSession();

    verify(session).save(sugerencia);
  }

  @Test
  public void alActualizarSugerenciaDebeUsarLaSesionActualYActualizarLaEntidad() {
    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    repositorio.actualizar(sugerencia);

    verify(sessionFactory).getCurrentSession();
    verify(session).update(sugerencia);
  }

  @Test
  public void alEliminarSugerenciaDebeUsarLaSesionActualYEliminarLaEntidad() {
    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    repositorio.eliminar(sugerencia);

    verify(sessionFactory).getCurrentSession();
    verify(session).delete(sugerencia);
  }

  @Test
  public void alBuscarPorIdDebeUsarLaSesionActualYRetornarLaSugerencia() {
    SugerenciaPregunta sugerenciaEsperada = mock(SugerenciaPregunta.class);

    when(session.get(SugerenciaPregunta.class, 1L)).thenReturn(sugerenciaEsperada);

    // Act:
    SugerenciaPregunta resultado = repositorio.buscarPorId(1L);

    assertEquals(sugerenciaEsperada, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).get(SugerenciaPregunta.class, 1L);
  }

  @Test
  public void alBuscarPorEstadoDebeCrearQueryConEstadoYRetornarResultado() {
    EstadoSugerenciaPregunta estado = EstadoSugerenciaPregunta.PENDIENTE;

    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);
    List<SugerenciaPregunta> sugerenciasEsperadas = List.of(sugerencia);

    Query<SugerenciaPregunta> query = crearQueryMock();

    String hql = "FROM SugerenciaPregunta s WHERE s.estado = :estado ORDER BY s.fechaCreacion DESC";

    when(session.createQuery(hql, SugerenciaPregunta.class)).thenReturn(query);

    when(query.setParameter("estado", estado)).thenReturn(query);

    when(query.getResultList()).thenReturn(sugerenciasEsperadas);

    List<SugerenciaPregunta> resultado = repositorio.buscarPorEstado(estado);

    assertEquals(sugerenciasEsperadas, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(hql, SugerenciaPregunta.class);
    verify(query).setParameter("estado", estado);
    verify(query).getResultList();
  }

  @Test
  public void alBuscarTodasDebeCrearQueryOrdenadaPorFechaYRetornarResultado() {
    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);
    List<SugerenciaPregunta> sugerenciasEsperadas = List.of(sugerencia);

    Query<SugerenciaPregunta> query = crearQueryMock();

    String hql = "FROM SugerenciaPregunta s ORDER BY s.fechaCreacion DESC";

    when(session.createQuery(hql, SugerenciaPregunta.class)).thenReturn(query);

    when(query.getResultList()).thenReturn(sugerenciasEsperadas);

    List<SugerenciaPregunta> resultado = repositorio.buscarTodas();

    assertEquals(sugerenciasEsperadas, resultado);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(hql, SugerenciaPregunta.class);
    verify(query).getResultList();
  }

  @SuppressWarnings("unchecked")
  private Query<SugerenciaPregunta> crearQueryMock() {
    return mock(Query.class);
  }
}
