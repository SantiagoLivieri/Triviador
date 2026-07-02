//package com.tallerwebi.controladores;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertInstanceOf;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
//import com.tallerwebi.entidades.Usuario;
//import java.util.Map;
//import javax.servlet.http.HttpSession;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.servlet.ModelAndView;
//
//@ExtendWith(MockitoExtension.class)
//public class ControladorLobbyTest {
//
//  @Mock
//  private HttpSession session;
//
//  @InjectMocks
//  private ControladorLobby controladorLobby;
//
//  @Test
//  public void alMostrarLobbyDebeRetornarVistaLobbyConDatosLobbyYUsuarioLogueado() {
//    // Preparación
//    Usuario usuarioLogueado = new Usuario();
//
//    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
//
//    // Ejecución
//    ModelAndView modelAndView = controladorLobby.mostrarLobby(session);
//
//    // Verificación
//    assertEquals("lobby", modelAndView.getViewName());
//
//    Map<String, Object> modelo = modelAndView.getModel();
//
//    assertInstanceOf(DatosLobby.class, modelo.get("datosLobby"));
//    assertSame(usuarioLogueado, modelo.get("usuario"));
//
//    verify(session).getAttribute("usuarioLogueado");
//  }
//}
