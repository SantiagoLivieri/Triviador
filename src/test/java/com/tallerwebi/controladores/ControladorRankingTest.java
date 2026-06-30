package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioRanking;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
public class ControladorRankingTest {

  @Mock
  private ServicioRanking servicioRanking;

  @Mock
  private HttpSession session;

  @InjectMocks
  private ControladorRanking controladorRanking;

  @Test
  public void alMostrarRankingDebeRetornarVistaRankingConTop10MiPuestoYUsuario() {
    // Preparación
    Usuario usuarioLogueado = new Usuario();
    Usuario usuarioUno = new Usuario();
    Usuario usuarioDos = new Usuario();

    List<Usuario> top10 = List.of(usuarioUno, usuarioDos);
    Long miPuesto = 5L;

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioRanking.obtenerTop10General()).thenReturn(top10);
    when(servicioRanking.calcularPuestoUsuario(usuarioLogueado)).thenReturn(miPuesto);

    // Ejecución
    ModelAndView modelAndView = controladorRanking.mostrarRanking(session);

    // Verificación
    assertEquals("ranking", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertSame(top10, modelo.get("top10"));
    assertEquals(miPuesto, modelo.get("miPuesto"));
    assertSame(usuarioLogueado, modelo.get("usuario"));

    verify(session).getAttribute("usuarioLogueado");
    verify(servicioRanking).obtenerTop10General();
    verify(servicioRanking).calcularPuestoUsuario(usuarioLogueado);
  }
}
