package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ExtendWith(MockitoExtension.class)
public class ControladorPreguntaTest {

  @Mock
  private ServicioJuego servicioJuego;

  @Mock
  private ServicioProvincia servicioProvincia;

  @Mock
  private ServicioPregunta servicioPregunta;

  @Mock
  private HttpSession session;

  @Mock
  private RedirectAttributes flash;

  @InjectMocks
  private ControladorPregunta controladorPregunta;

  @Test
  public void alSeleccionarProvinciaDebeIniciarAtaqueGuardarPreguntaEnSesionYRedirigirAPreguntaActual()
    throws TiempoAgotadoException, TurnoInvalidoException {
    Long partidaId = 1L;
    Long idProvincia = 2L;

    Pregunta pregunta = crearPregunta(10L);
    Set<Long> preguntasHechas = new HashSet<>();
    List<String> opciones = List.of("Correcta", "Incorrecta 1", "Incorrecta 2", "Incorrecta 3");

    when(servicioJuego.obtenerPreguntasHechas(partidaId)).thenReturn(preguntasHechas);
    when(servicioPregunta.obtenerPreguntaPorProvincia(idProvincia, preguntasHechas))
      .thenReturn(pregunta);
    when(servicioJuego.obtenerCantidadPreguntasRequeridas(idProvincia)).thenReturn(3);
    when(servicioPregunta.obtenerOpcionesMezcladas(pregunta)).thenReturn(opciones);

    ModelAndView modelAndView = controladorPregunta.seleccionarProvincia(
      idProvincia,
      partidaId,
      session
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(servicioJuego).iniciarAtaque(partidaId, idProvincia);
    verify(servicioJuego).registrarPreguntaHecha(partidaId, 10L);

    verify(session).setAttribute("preguntasRequeridas", 3);
    verify(session).setAttribute("preguntasRespondidasExito", 0);
    verify(session).setAttribute("preguntaActual", pregunta);
    verify(session).setAttribute("opcionesActuales", opciones);
    verify(session).setAttribute("idProvinciaActual", idProvincia);

    verify(session).removeAttribute("comodinUsadoEnEstaPregunta");
    verify(session).removeAttribute("dobleChanceActivo");
  }

  @Test
  public void alSeleccionarProvinciaSinPreguntaDebeVolverAlTableroConMensaje()
    throws TiempoAgotadoException, TurnoInvalidoException {
    Long partidaId = 1L;
    Long idProvincia = 2L;

    Set<Long> preguntasHechas = new HashSet<>();

    when(servicioJuego.obtenerPreguntasHechas(partidaId)).thenReturn(preguntasHechas);
    when(servicioPregunta.obtenerPreguntaPorProvincia(idProvincia, preguntasHechas))
      .thenReturn(null);

    ModelAndView modelAndView = controladorPregunta.seleccionarProvincia(
      idProvincia,
      partidaId,
      session
    );

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());

    verify(session).setAttribute("mensajeResultado", "No hay preguntas cargadas.");
  }

  @Test
  public void alSeleccionarProvinciaConErrorDebeVolverAlTableroConMensajeDeError()
    throws TiempoAgotadoException, TurnoInvalidoException {
    Long partidaId = 1L;
    Long idProvincia = 2L;

    doThrow(new IllegalArgumentException("No podes atacar tu propia provincia."))
      .when(servicioJuego)
      .iniciarAtaque(partidaId, idProvincia);

    ModelAndView modelAndView = controladorPregunta.seleccionarProvincia(
      idProvincia,
      partidaId,
      session
    );

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());

    verify(session).setAttribute("mensajeResultado", "No podes atacar tu propia provincia.");
  }

  @Test
  public void alMostrarPreguntaActualSinPreguntaEnSesionDebeRedirigirAlTablero() {
    Long partidaId = 1L;

    when(session.getAttribute("preguntaActual")).thenReturn(null);

    ModelAndView modelAndView = controladorPregunta.mostrarPreguntaActual(partidaId, session);

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());
  }

  @Test
  public void alMostrarPreguntaActualConProvinciaInexistenteDebeRedirigirAlTableroConMensaje() {
    Long partidaId = 1L;

    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("idProvinciaActual")).thenReturn(2L);
    when(servicioProvincia.buscarPorId(2L)).thenReturn(null);

    ModelAndView modelAndView = controladorPregunta.mostrarPreguntaActual(partidaId, session);

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());
    verify(session).setAttribute("mensajeResultado", "No se encontro la provincia seleccionada.");
  }

  @Test
  public void alMostrarPreguntaActualDebeRetornarVistaPreguntaConModeloCompleto() {
    Long partidaId = 1L;

    Pregunta pregunta = crearPregunta(10L);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);
    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugador = org.mockito.Mockito.mock(Jugador.class);

    List<String> opciones = List.of("Correcta", "Incorrecta 1");

    when(provincia.getId()).thenReturn(2L);
    when(partida.getJugadorEnTurno()).thenReturn(jugador);

    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("idProvinciaActual")).thenReturn(2L);
    when(session.getAttribute("opcionesActuales")).thenReturn(opciones);

    when(servicioProvincia.buscarPorId(2L)).thenReturn(provincia);
    when(servicioJuego.obtenerPartidaPorId(partidaId)).thenReturn(partida);

    ModelAndView modelAndView = controladorPregunta.mostrarPreguntaActual(partidaId, session);

    assertEquals("pregunta", modelAndView.getViewName());

    Map<String, Object> modelo = modelAndView.getModel();

    assertSame(partida, modelo.get("partida"));
    assertEquals(partidaId, modelo.get("partidaId"));
    assertSame(pregunta, modelo.get("pregunta"));
    assertSame(jugador, modelo.get("jugadorActual"));
    assertEquals(2L, modelo.get("idProvincia"));
    assertSame(opciones, modelo.get("opciones"));
    assertSame(provincia, modelo.get("provincia"));
  }

  @Test
  public void alResponderIncorrectoSinDobleChanceDebeAvanzarTurnoYVolverAlTablero() {
    Long partidaId = 1L;
    Long idProvincia = 2L;
    Long idPregunta = 10L;

    when(servicioPregunta.validarRespuesta(idPregunta, "Mal")).thenReturn(false);
    when(session.getAttribute("dobleChanceActivo")).thenReturn(null);
    when(session.getAttribute("usuarioId")).thenReturn(5L);
    when(servicioJuego.evaluarYFinalizarPartida(partidaId, 5L)).thenReturn(false);

    ModelAndView modelAndView = controladorPregunta.responderProvincia(
      partidaId,
      idProvincia,
      idPregunta,
      "Mal",
      session,
      flash
    );

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());

    verify(session).setAttribute("mensajeResultado", "Respuesta incorrecta. Fin de tu turno.");
    verify(session).removeAttribute("preguntasRequeridas");
    verify(session).removeAttribute("preguntasRespondidasExito");
    verify(servicioJuego).avanzarTurno(partidaId);
  }

  @Test
  public void alResponderIncorrectoConDobleChanceDebeRemoverOpcionYVolverAPreguntaActual() {
    Long partidaId = 1L;
    Long idProvincia = 2L;
    Long idPregunta = 10L;

    List<String> opciones = List.of("Correcta", "Incorrecta 1", "Incorrecta 2");
    List<String> opcionesFiltradas = List.of("Correcta", "Incorrecta 2");

    when(servicioPregunta.validarRespuesta(idPregunta, "Incorrecta 1")).thenReturn(false);
    when(session.getAttribute("dobleChanceActivo")).thenReturn(true);
    when(session.getAttribute("opcionesActuales")).thenReturn(opciones);
    when(servicioPregunta.removerOpcionIncorrecta(opciones, "Incorrecta 1"))
      .thenReturn(opcionesFiltradas);

    ModelAndView modelAndView = controladorPregunta.responderProvincia(
      partidaId,
      idProvincia,
      idPregunta,
      "Incorrecta 1",
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(session).setAttribute("dobleChanceActivo", false);
    verify(session).setAttribute("opcionesActuales", opcionesFiltradas);
    verify(flash)
      .addFlashAttribute(
        "mensajeComodin",
        "¡Respuesta incorrecta, pero la Doble Chance te salvó! Te queda un intento."
      );
  }

  @Test
  public void alResponderCorrectamenteYConquistarProvinciaDebeVolverAlTablero() {
    Long partidaId = 1L;
    Long idProvincia = 2L;
    Long idPregunta = 10L;

    when(servicioPregunta.validarRespuesta(idPregunta, "Correcta")).thenReturn(true);
    when(session.getAttribute("preguntasRespondidasExito")).thenReturn(2);
    when(session.getAttribute("preguntasRequeridas")).thenReturn(3);

    when(servicioJuego.evaluarAcierto(partidaId, idProvincia, 3, 3))
      .thenReturn("Provincia conquistada.");

    when(session.getAttribute("usuarioId")).thenReturn(5L);
    when(servicioJuego.evaluarYFinalizarPartida(partidaId, 5L)).thenReturn(false);

    ModelAndView modelAndView = controladorPregunta.responderProvincia(
      partidaId,
      idProvincia,
      idPregunta,
      "Correcta",
      session,
      flash
    );

    assertEquals("redirect:/juego?id=1", modelAndView.getViewName());

    verify(session).setAttribute("preguntasRespondidasExito", 3);
    verify(session).setAttribute("mensajeResultado", "Provincia conquistada.");
    verify(session).removeAttribute("preguntasRequeridas");
    verify(session).removeAttribute("preguntasRespondidasExito");
  }

  @Test
  public void alResponderCorrectamenteSinConquistarDebePrepararProximaPregunta() {
    Long partidaId = 1L;
    Long idProvincia = 2L;
    Long idPregunta = 10L;

    Pregunta proximaPregunta = crearPregunta(20L);
    Set<Long> preguntasHechas = new HashSet<>();
    List<String> opciones = List.of("Correcta", "Incorrecta 1");

    when(servicioPregunta.validarRespuesta(idPregunta, "Correcta")).thenReturn(true);
    when(session.getAttribute("preguntasRespondidasExito")).thenReturn(null);
    when(session.getAttribute("preguntasRequeridas")).thenReturn(3);

    when(servicioJuego.evaluarAcierto(partidaId, idProvincia, 1, 3)).thenReturn(null);
    when(servicioJuego.obtenerPreguntasHechas(partidaId)).thenReturn(preguntasHechas);
    when(servicioPregunta.obtenerPreguntaPorProvincia(idProvincia, preguntasHechas))
      .thenReturn(proximaPregunta);
    when(servicioPregunta.obtenerOpcionesMezcladas(proximaPregunta)).thenReturn(opciones);

    ModelAndView modelAndView = controladorPregunta.responderProvincia(
      partidaId,
      idProvincia,
      idPregunta,
      "Correcta",
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(session).setAttribute("preguntasRespondidasExito", 1);
    verify(servicioJuego).registrarPreguntaHecha(partidaId, 20L);
    verify(session).setAttribute("preguntaActual", proximaPregunta);
    verify(session).setAttribute("opcionesActuales", opciones);
    verify(session).removeAttribute("comodinUsadoEnEstaPregunta");
    verify(session).removeAttribute("dobleChanceActivo");
  }

  @Test
  public void alUsarComodinSiEsJugadorInvitadoDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorInvitado = org.mockito.Mockito.mock(Jugador.class);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(servicioJuego.obtenerPartidaPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorInvitado);
    when(jugadorInvitado.getUsuario()).thenReturn(null);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "ELIMINAR_2",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "Solo el anfitrión puede gastar comodines.");
  }

  @Test
  public void alUsarComodinSinPreguntaActivaDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(null);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "ELIMINAR_2",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "No hay pregunta activa.");
  }

  @Test
  public void alUsarComodinSiYaUsoUnoEnLaPreguntaDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(true);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "ELIMINAR_2",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "Ya utilizaste un comodín en esta pregunta.");
  }

  @Test
  public void alUsarComodinEliminarDosDebeActualizarOpcionesSesionUsuarioYMostrarMensaje() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Usuario usuarioActualizado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    List<String> opciones = List.of("Correcta", "Incorrecta 1", "Incorrecta 2", "Incorrecta 3");
    List<String> opcionesFiltradas = List.of("Correcta", "Incorrecta 2");

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("opcionesActuales")).thenReturn(opciones);

    when(servicioPregunta.aplicarComodinEliminarDos(5L, opciones, pregunta))
      .thenReturn(opcionesFiltradas);

    when(flash.getFlashAttributes()).thenReturn(Map.of());

    when(servicioJuego.obtenerUsuarioPorId(5L)).thenReturn(usuarioActualizado);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "ELIMINAR_2",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(session).setAttribute("opcionesActuales", opcionesFiltradas);
    verify(session).setAttribute("comodinUsadoEnEstaPregunta", true);
    verify(flash)
      .addFlashAttribute(
        "mensajeComodin",
        "¡Comodín aplicado! Se eliminaron 2 respuestas incorrectas."
      );
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alUsarComodinEliminarDosConDosOpcionesDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    List<String> opciones = List.of("Correcta", "Incorrecta 1");

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("opcionesActuales")).thenReturn(opciones);

    doReturn(Map.of("errorComodin", "Ya usaste este comodín.")).when(flash).getFlashAttributes();

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "ELIMINAR_2",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "Ya usaste este comodín.");
  }

  @Test
  public void alUsarComodinDobleChanceDebeActivarloYActualizarUsuario() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Usuario usuarioActualizado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("dobleChanceActivo")).thenReturn(false);

    when(flash.getFlashAttributes()).thenReturn(Map.of());
    when(servicioJuego.obtenerUsuarioPorId(5L)).thenReturn(usuarioActualizado);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "DOBLE_CHANCE",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(servicioPregunta).aplicarComodinDobleChance(5L);
    verify(session).setAttribute("dobleChanceActivo", true);
    verify(session).setAttribute("comodinUsadoEnEstaPregunta", true);
    verify(flash).addFlashAttribute("mensajeComodin", "¡Doble Chance activada!");
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alUsarComodinDobleChanceSiYaEstaActivaDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("dobleChanceActivo")).thenReturn(true);

    doReturn(Map.of("errorComodin", "Ya tenes la Doble Chance activada para este turno."))
      .when(flash)
      .getFlashAttributes();

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "DOBLE_CHANCE",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash)
      .addFlashAttribute("errorComodin", "Ya tenes la Doble Chance activada para este turno.");
  }

  @Test
  public void alUsarComodinPasarPreguntaDebeCambiarPreguntaOpcionesYGuardarHistorial() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Usuario usuarioActualizado = crearUsuario(5L);

    Pregunta preguntaActual = crearPregunta(10L);
    Pregunta nuevaPregunta = crearPregunta(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();
    List<String> nuevasOpciones = List.of("Nueva correcta", "Nueva incorrecta");

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(preguntaActual);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("preguntasYaHechas")).thenReturn(preguntasYaHechas);
    when(session.getAttribute("idProvinciaActual")).thenReturn(2L);

    when(servicioPregunta.aplicarComodinPasarPregunta(5L, preguntaActual, 2L, preguntasYaHechas))
      .thenReturn(nuevaPregunta);

    when(servicioPregunta.obtenerOpcionesMezcladas(nuevaPregunta)).thenReturn(nuevasOpciones);

    when(flash.getFlashAttributes()).thenReturn(Map.of());
    when(servicioJuego.obtenerUsuarioPorId(5L)).thenReturn(usuarioActualizado);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "PASAR_PREGUNTA",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(session).setAttribute("preguntaActual", nuevaPregunta);
    verify(session).setAttribute("opcionesActuales", nuevasOpciones);
    verify(session).setAttribute("preguntasYaHechas", preguntasYaHechas);
    verify(session).setAttribute("comodinUsadoEnEstaPregunta", true);
    verify(flash).addFlashAttribute("mensajeComodin", "¡Pregunta saltada exitosamente!");
    verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
  }

  @Test
  public void alUsarComodinDesconocidoDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);

    doReturn(Map.of("errorComodin", "El tipo de comodín no es reconocido."))
      .when(flash)
      .getFlashAttributes();

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "COMODIN_RARO",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "El tipo de comodín no es reconocido.");
  }

  @Test
  public void alUsarComodinYElServicioLanzaExcepcionDebeMostrarError() {
    Long partidaId = 1L;

    Usuario usuarioLogueado = crearUsuario(5L);
    Pregunta pregunta = crearPregunta(10L);

    when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
    when(session.getAttribute("preguntaActual")).thenReturn(pregunta);
    when(session.getAttribute("comodinUsadoEnEstaPregunta")).thenReturn(null);
    when(session.getAttribute("dobleChanceActivo")).thenReturn(false);

    doThrow(new IllegalArgumentException("No tenes ese comodín."))
      .when(servicioPregunta)
      .aplicarComodinDobleChance(5L);

    ModelAndView modelAndView = controladorPregunta.usarComodin(
      "DOBLE_CHANCE",
      partidaId,
      session,
      flash
    );

    assertEquals("redirect:/disputa/pregunta-actual?partidaId=1", modelAndView.getViewName());

    verify(flash).addFlashAttribute("errorComodin", "No tenes ese comodín.");
  }

  private Pregunta crearPregunta(Long id) {
    Pregunta pregunta = new Pregunta(
      "Pregunta",
      "Correcta",
      "Incorrecta 1",
      "Incorrecta 2",
      "Incorrecta 3",
      null,
      null,
      null
    );
    pregunta.setId(id);
    return pregunta;
  }

  private Usuario crearUsuario(Long id) {
    Usuario usuario = new Usuario();
    usuario.setId(id);
    return usuario;
  }
}
