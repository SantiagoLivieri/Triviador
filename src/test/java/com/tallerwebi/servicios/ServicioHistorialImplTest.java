package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.repositorios.RepositorioHistorial;
import com.tallerwebi.servicios.Impl.ServicioHistorialImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioHistorialImplTest {

  @Mock
  private RepositorioHistorial repositorioHistorial;

  @InjectMocks
  private ServicioHistorialImpl servicioHistorial;

  @Test
  public void alBuscarHistorialPorUsuarioDebeRetornarLoQueDevuelveElRepositorio() {
    // Preparación
    Long usuarioId = 1L;

    HistorialPartida historialUno = org.mockito.Mockito.mock(HistorialPartida.class);
    HistorialPartida historialDos = org.mockito.Mockito.mock(HistorialPartida.class);

    List<HistorialPartida> historialEsperado = List.of(historialUno, historialDos);

    when(repositorioHistorial.buscarPorUsuarioId(usuarioId)).thenReturn(historialEsperado);

    // Ejecución
    List<HistorialPartida> historialObtenido = servicioHistorial.buscarHistorialPorUsuario(
      usuarioId
    );

    // Verificación
    assertSame(historialEsperado, historialObtenido);
    verify(repositorioHistorial).buscarPorUsuarioId(usuarioId);
  }

  @Test
  public void alGuardarHistorialDebeLlamarAlRepositorio() {
    // Preparación
    HistorialPartida historial = org.mockito.Mockito.mock(HistorialPartida.class);

    // Ejecución
    servicioHistorial.guardar(historial);

    // Verificación
    verify(repositorioHistorial).guardar(historial);
  }
}
