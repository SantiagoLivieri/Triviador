package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioHistorial;
import com.tallerwebi.servicios.ServicioUsuario;
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
public class ControladorPerfilTest {

  @Mock
  private ServicioUsuario servicioUsuario;

  @Mock
  private ServicioHistorial servicioHistorial;

  @Mock
  private HttpSession session;

  @InjectMocks
  private ControladorPerfil controladorPerfil;

  @Test
  public void alVerPerfilDebeRetornarVistaPerfilConUsuarioEHistorial() {
    // Preparación
    Long usuarioId = 1L;

    Usuario usuario = new Usuario();

    HistorialPartida historialUno = org.mockito.Mockito.mock(HistorialPartida.class);
    HistorialPartida historialDos = org.mockito.Mockito.mock(HistorialPartida.class);

    List<HistorialPartida> historial = List.of(historialUno, historialDos);

    when(session.getAttribute("usuarioId")).thenReturn(usuarioId);
    when(servicioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);
    when(servicioHistorial.buscarHistorialPorUsuario(usuarioId)).thenReturn(historial);

    // Ejecución
    ModelAndView modelAndView = controladorPerfil.verPerfil(session);

    // Verificación
    assertEquals("perfil", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertSame(usuario, modelo.get("usuario"));
    assertSame(historial, modelo.get("historial"));

    verify(session).getAttribute("usuarioId");
    verify(servicioUsuario).buscarUsuarioPorId(usuarioId);
    verify(servicioHistorial).buscarHistorialPorUsuario(usuarioId);
  }

  @Test
  public void alGuardarPerfilDebeActualizarPerfilActualizarUsuarioEnSesionYRedirigirAPerfil() {
    // Preparación
    Long usuarioId = 1L;
    String nombre = "Santiago";
    String nombreJugador = "Santi";

    Usuario usuarioActualizado = new Usuario();

    when(session.getAttribute("usuarioId")).thenReturn(usuarioId);
    when(servicioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioActualizado);

    // Ejecución
    ModelAndView modelAndView = controladorPerfil.guardarPerfil(nombre, nombreJugador, session);

    // Verificación
    assertEquals("redirect:/perfil", modelAndView.getViewName());

    verify(session).getAttribute("usuarioId");
    verify(servicioUsuario).actualizarPerfil(usuarioId, nombre, nombreJugador);
    verify(servicioUsuario).buscarUsuarioPorId(usuarioId);
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }
}
