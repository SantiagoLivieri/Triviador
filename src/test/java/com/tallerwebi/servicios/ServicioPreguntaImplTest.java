package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.servicios.Impl.ServicioPreguntaImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioPreguntaImplTest {

  @Mock
  private RepositorioPregunta repositorioPregunta;

  @Mock
  private ServicioUsuario servicioUsuario;

  @InjectMocks
  private ServicioPreguntaImpl servicioPregunta;

  @Test
  public void alObtenerPreguntaPorProvinciaDebeRetornarUnaPreguntaDisponibleDeEsaProvincia() {
    // Preparación
    Long idProvincia = 1L;

    Pregunta preguntaYaHecha = crearPreguntaConId(10L);
    Pregunta preguntaDisponible = crearPreguntaConId(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();
    preguntasYaHechas.add(10L);

    when(repositorioPregunta.buscarPorProvincia(idProvincia))
      .thenReturn(List.of(preguntaYaHecha, preguntaDisponible));

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.obtenerPreguntaPorProvincia(
      idProvincia,
      preguntasYaHechas
    );

    // Verificación
    assertSame(preguntaDisponible, preguntaObtenida);
    verify(repositorioPregunta).buscarPorProvincia(idProvincia);
    verify(repositorioPregunta, never()).buscarTodas();
  }

  @Test
  public void alObtenerPreguntaPorProvinciaSinDisponiblesDebeBuscarEnTodasLasPreguntas() {
    // Preparación
    Long idProvincia = 1L;

    Pregunta preguntaDeProvinciaYaHecha = crearPreguntaConId(10L);
    Pregunta preguntaGeneralDisponible = crearPreguntaConId(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();
    preguntasYaHechas.add(10L);

    when(repositorioPregunta.buscarPorProvincia(idProvincia))
      .thenReturn(List.of(preguntaDeProvinciaYaHecha));
    when(repositorioPregunta.buscarTodas()).thenReturn(List.of(preguntaGeneralDisponible));

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.obtenerPreguntaPorProvincia(
      idProvincia,
      preguntasYaHechas
    );

    // Verificación
    assertSame(preguntaGeneralDisponible, preguntaObtenida);
    verify(repositorioPregunta).buscarPorProvincia(idProvincia);
    verify(repositorioPregunta).buscarTodas();
  }

  @Test
  public void alObtenerPreguntaPorProvinciaSinPreguntasEnBaseDebeLanzarIllegalArgumentException() {
    // Preparación
    Long idProvincia = 1L;
    Set<Long> preguntasYaHechas = new HashSet<>();

    when(repositorioPregunta.buscarPorProvincia(idProvincia)).thenReturn(List.of());
    when(repositorioPregunta.buscarTodas()).thenReturn(List.of());

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioPregunta.obtenerPreguntaPorProvincia(idProvincia, preguntasYaHechas)
    );

    verify(repositorioPregunta).buscarPorProvincia(idProvincia);
    verify(repositorioPregunta).buscarTodas();
  }

  @Test
  public void alObtenerPreguntaPorProvinciaSiTodasFueronHechasDebeLimpiarSetYRetornarUnaPregunta() {
    // Preparación
    Long idProvincia = 1L;

    Pregunta preguntaUno = crearPreguntaConId(10L);
    Pregunta preguntaDos = crearPreguntaConId(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();
    preguntasYaHechas.add(10L);
    preguntasYaHechas.add(20L);

    /*
     * Importante:
     *
     * En este caso el servicio ejecuta:
     *
     * Collections.shuffle(todasLasPreguntas);
     *
     * Por eso buscarTodas() NO puede devolver List.of(...),
     * porque List.of(...) crea una lista inmutable.
     *
     * Usamos ArrayList para que shuffle pueda modificar el orden.
     */
    List<Pregunta> todasLasPreguntas = new ArrayList<>(List.of(preguntaUno, preguntaDos));

    when(repositorioPregunta.buscarPorProvincia(idProvincia)).thenReturn(List.of(preguntaUno));
    when(repositorioPregunta.buscarTodas()).thenReturn(todasLasPreguntas);

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.obtenerPreguntaPorProvincia(
      idProvincia,
      preguntasYaHechas
    );

    // Verificación
    assertTrue(List.of(preguntaUno, preguntaDos).contains(preguntaObtenida));
    assertTrue(preguntasYaHechas.isEmpty());

    verify(repositorioPregunta).buscarPorProvincia(idProvincia);
    verify(repositorioPregunta).buscarTodas();
  }

  @Test
  public void alObtenerOpcionesMezcladasConPreguntaNullDebeLanzarIllegalArgumentException() {
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioPregunta.obtenerOpcionesMezcladas(null)
    );
  }

  @Test
  public void alObtenerOpcionesMezcladasDebeRetornarLasCuatroOpciones() {
    // Preparación
    Pregunta pregunta = crearPreguntaConOpciones();

    // Ejecución
    List<String> opciones = servicioPregunta.obtenerOpcionesMezcladas(pregunta);

    // Verificación
    assertEquals(4, opciones.size());
    assertTrue(opciones.contains("Correcta"));
    assertTrue(opciones.contains("Incorrecta 1"));
    assertTrue(opciones.contains("Incorrecta 2"));
    assertTrue(opciones.contains("Incorrecta 3"));
  }

  @Test
  public void alBuscarPorIdDebeRetornarLaPreguntaDelRepositorio() {
    // Preparación
    Long preguntaId = 1L;
    Pregunta preguntaEsperada = crearPreguntaConId(preguntaId);

    when(repositorioPregunta.buscarPorId(preguntaId)).thenReturn(preguntaEsperada);

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.buscarPorId(preguntaId);

    // Verificación
    assertSame(preguntaEsperada, preguntaObtenida);
    verify(repositorioPregunta).buscarPorId(preguntaId);
  }

  @Test
  public void validarRespuestaConPreguntaInexistenteDebeLanzarIllegalArgumentException() {
    // Preparación
    Long preguntaId = 99L;

    when(repositorioPregunta.buscarPorId(preguntaId)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioPregunta.validarRespuesta(preguntaId, "Respuesta")
    );

    verify(repositorioPregunta).buscarPorId(preguntaId);
  }

  @Test
  public void validarRespuestaCorrectaDebeRetornarTrueIgnorandoMayusculasYEspacios() {
    // Preparación
    Long preguntaId = 1L;
    Pregunta pregunta = crearPreguntaConOpciones();

    when(repositorioPregunta.buscarPorId(preguntaId)).thenReturn(pregunta);

    // Ejecución
    boolean resultado = servicioPregunta.validarRespuesta(preguntaId, "  correcta  ");

    // Verificación
    assertTrue(resultado);
    verify(repositorioPregunta).buscarPorId(preguntaId);
  }

  @Test
  public void validarRespuestaIncorrectaDebeRetornarFalse() {
    // Preparación
    Long preguntaId = 1L;
    Pregunta pregunta = crearPreguntaConOpciones();

    when(repositorioPregunta.buscarPorId(preguntaId)).thenReturn(pregunta);

    // Ejecución
    boolean resultado = servicioPregunta.validarRespuesta(preguntaId, "Incorrecta");

    // Verificación
    assertFalse(resultado);
    verify(repositorioPregunta).buscarPorId(preguntaId);
  }

  @Test
  public void aplicarComodinEliminarDosConUsuarioInexistenteDebeLanzarIllegalArgumentException() {
    // Preparación
    Long idUsuario = 1L;
    Pregunta pregunta = crearPreguntaConOpciones();

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () ->
        servicioPregunta.aplicarComodinEliminarDos(
          idUsuario,
          List.of("Correcta", "Incorrecta 1", "Incorrecta 2", "Incorrecta 3"),
          pregunta
        )
    );

    verify(servicioUsuario).buscarUsuarioPorId(idUsuario);
    verify(servicioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void aplicarComodinEliminarDosDebeConsumirComodinActualizarUsuarioYRetornarDosOpciones() {
    // Preparación
    Long idUsuario = 1L;

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);
    Pregunta pregunta = crearPreguntaConOpciones();

    List<String> opcionesEnPantalla = List.of(
      "Correcta",
      "Incorrecta 1",
      "Incorrecta 2",
      "Incorrecta 3"
    );

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);

    // Ejecución
    List<String> opcionesSobrevivientes = servicioPregunta.aplicarComodinEliminarDos(
      idUsuario,
      opcionesEnPantalla,
      pregunta
    );

    // Verificación
    assertEquals(2, opcionesSobrevivientes.size());
    assertTrue(opcionesSobrevivientes.contains("Correcta"));

    verify(usuario).consumirComodin("ELIMINAR_2");
    verify(servicioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void aplicarComodinEliminarDosSinIncorrectasEnPantallaDebeRetornarSoloLaCorrecta() {
    // Preparación
    Long idUsuario = 1L;

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);
    Pregunta pregunta = crearPreguntaConOpciones();

    List<String> opcionesEnPantalla = List.of("Correcta");

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);

    // Ejecución
    List<String> opcionesSobrevivientes = servicioPregunta.aplicarComodinEliminarDos(
      idUsuario,
      opcionesEnPantalla,
      pregunta
    );

    // Verificación
    assertEquals(1, opcionesSobrevivientes.size());
    assertEquals("Correcta", opcionesSobrevivientes.get(0));

    verify(usuario).consumirComodin("ELIMINAR_2");
    verify(servicioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void aplicarComodinDobleChanceConUsuarioInexistenteDebeLanzarIllegalArgumentException() {
    // Preparación
    Long idUsuario = 1L;

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioPregunta.aplicarComodinDobleChance(idUsuario)
    );

    verify(servicioUsuario).buscarUsuarioPorId(idUsuario);
    verify(servicioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void aplicarComodinDobleChanceDebeConsumirComodinYActualizarUsuario() {
    // Preparación
    Long idUsuario = 1L;
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);

    // Ejecución
    servicioPregunta.aplicarComodinDobleChance(idUsuario);

    // Verificación
    verify(usuario).consumirComodin("DOBLE_CHANCE");
    verify(servicioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void aplicarComodinPasarPreguntaConUsuarioInexistenteDebeLanzarIllegalArgumentException() {
    // Preparación
    Long idUsuario = 1L;
    Long idProvincia = 2L;
    Pregunta preguntaActual = crearPreguntaConId(10L);
    Set<Long> preguntasYaHechas = new HashSet<>();

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () ->
        servicioPregunta.aplicarComodinPasarPregunta(
          idUsuario,
          preguntaActual,
          idProvincia,
          preguntasYaHechas
        )
    );

    verify(servicioUsuario).buscarUsuarioPorId(idUsuario);
    verify(servicioUsuario, never()).actualizarUsuario(any(Usuario.class));
  }

  @Test
  public void aplicarComodinPasarPreguntaConPreguntaActualDebeAgregarlaAHechasYRetornarOtraPregunta() {
    // Preparación
    Long idUsuario = 1L;
    Long idProvincia = 2L;

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    Pregunta preguntaActual = crearPreguntaConId(10L);
    Pregunta preguntaNueva = crearPreguntaConId(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
    when(repositorioPregunta.buscarPorProvincia(idProvincia)).thenReturn(List.of(preguntaNueva));

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.aplicarComodinPasarPregunta(
      idUsuario,
      preguntaActual,
      idProvincia,
      preguntasYaHechas
    );

    // Verificación
    assertSame(preguntaNueva, preguntaObtenida);
    assertTrue(preguntasYaHechas.contains(10L));

    verify(usuario).consumirComodin("PASAR_PREGUNTA");
    verify(servicioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void aplicarComodinPasarPreguntaConPreguntaActualNullNoDebeAgregarNadaAHechas() {
    // Preparación
    Long idUsuario = 1L;
    Long idProvincia = 2L;

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);
    Pregunta preguntaNueva = crearPreguntaConId(20L);

    Set<Long> preguntasYaHechas = new HashSet<>();

    when(servicioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
    when(repositorioPregunta.buscarPorProvincia(idProvincia)).thenReturn(List.of(preguntaNueva));

    // Ejecución
    Pregunta preguntaObtenida = servicioPregunta.aplicarComodinPasarPregunta(
      idUsuario,
      null,
      idProvincia,
      preguntasYaHechas
    );

    // Verificación
    assertSame(preguntaNueva, preguntaObtenida);
    assertTrue(preguntasYaHechas.isEmpty());

    verify(usuario).consumirComodin("PASAR_PREGUNTA");
    verify(servicioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void removerOpcionIncorrectaConListaDebeEliminarLaRespuestaIncorrecta() {
    // Preparación
    List<String> opciones = new ArrayList<>(List.of("Correcta", "Incorrecta 1", "Incorrecta 2"));

    // Ejecución
    List<String> resultado = servicioPregunta.removerOpcionIncorrecta(opciones, "Incorrecta 1");

    // Verificación
    assertSame(opciones, resultado);
    assertFalse(resultado.contains("Incorrecta 1"));
    assertEquals(2, resultado.size());
  }

  @Test
  public void removerOpcionIncorrectaConListaNullDebeRetornarNull() {
    // Ejecución
    List<String> resultado = servicioPregunta.removerOpcionIncorrecta(null, "Incorrecta 1");

    // Verificación
    assertNull(resultado);
  }

  private Pregunta crearPreguntaConId(Long id) {
    Pregunta pregunta = crearPreguntaConOpciones();
    pregunta.setId(id);
    return pregunta;
  }

  private Pregunta crearPreguntaConOpciones() {
    return new Pregunta(
      "Pregunta de prueba",
      "Correcta",
      "Incorrecta 1",
      "Incorrecta 2",
      "Incorrecta 3",
      null,
      null,
      null
    );
  }
}
