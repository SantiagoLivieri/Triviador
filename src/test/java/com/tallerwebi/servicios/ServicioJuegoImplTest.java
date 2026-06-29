package com.tallerwebi.servicios;

// JUnit: métodos para verificar resultados.
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
// Mockito: acepta cualquier valor del tipo indicado.
import static org.mockito.ArgumentMatchers.any;
// Mockito: crea un mock manualmente.
import static org.mockito.Mockito.mock;
// Mockito: verifica que un método NO haya sido llamado.
import static org.mockito.Mockito.never;
// Mockito: verifica que un método haya sido llamado.
import static org.mockito.Mockito.verify;
// Mockito: define qué devuelve un mock cuando se llama a un método.
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.Impl.ServicioJuegoImpl;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
// JUnit: marca un método como test.
import org.junit.jupiter.api.Test;
// JUnit: permite extender el comportamiento de JUnit.
// En este caso lo usamos para conectar JUnit con Mockito.
import org.junit.jupiter.api.extension.ExtendWith;
// Mockito: crea e inyecta automáticamente los mocks en la clase real.
import org.mockito.InjectMocks;
// Mockito: crea objetos simulados.
import org.mockito.Mock;
// Mockito: extensión que permite usar @Mock y @InjectMocks con JUnit 5.
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * JUnit + Mockito:
 *
 * @ExtendWith(MockitoExtension.class) conecta JUnit 5 con Mockito.
 *
 * Gracias a esto, Mockito inicializa automáticamente:
 * - los @Mock
 * - el @InjectMocks
 *
 * Sin esta anotación, los mocks podrían quedar en null.
 */
@ExtendWith(MockitoExtension.class)
public class ServicioJuegoImplTest {

  /*
   * Mockito:
   *
   * @Mock crea una versión falsa de ServicioJugador.
   *
   * No queremos usar el servicio real porque este test es unitario.
   * Queremos probar solamente ServicioJuegoImpl.
   */
  @Mock
  private ServicioJugador servicioJugador;

  /*
   * Mockito:
   *
   * ServicioProvincia también es una dependencia de ServicioJuegoImpl.
   * La simulamos para controlar qué devuelve en cada test.
   */
  @Mock
  private ServicioProvincia servicioProvincia;

  /*
   * Mockito:
   *
   * ServicioPartida es una dependencia clave.
   * En varios tests vamos a simular que devuelve una partida.
   */
  @Mock
  private ServicioPartida servicioPartida;

  /*
   * Mockito:
   *
   * ServicioHistorial queda mockeado porque ServicioJuegoImpl lo necesita
   * en el constructor, aunque no todos los tests lo usen.
   */
  @Mock
  private ServicioHistorial servicioHistorial;

  /*
   * Mockito:
   *
   * ServicioUsuario queda mockeado para probar métodos que buscan
   * o actualizan usuarios sin tocar base de datos.
   */
  @Mock
  private ServicioUsuario servicioUsuario;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioJuegoImpl.
   *
   * Luego le inyecta automáticamente los mocks anteriores:
   * - servicioJugador
   * - servicioProvincia
   * - servicioPartida
   * - servicioHistorial
   * - servicioUsuario
   *
   * Esta es la clase real que estamos testeando.
   */
  @InjectMocks
  private ServicioJuegoImpl servicioJuego;

  @Test
  public void alAvanzarTurnoDebeActualizarLaPartida() {
    /*
     * Estructura típica de un test:
     *
     * 1. Preparación
     * 2. Ejecución
     * 3. Verificación
     */

    // Preparación
    Long partidaId = 1L;

    /*
     * Mockito:
     *
     * mock(Partida.class) crea una partida falsa.
     *
     * No usamos una Partida real porque acá no estamos testeando
     * la lógica interna de Partida, sino el comportamiento de ServicioJuegoImpl.
     */
    Partida partida = mock(Partida.class);

    /*
     * Mockito:
     *
     * when(...).thenReturn(...)
     *
     * Significa:
     * "Cuando se llame a servicioPartida.buscarPorId(partidaId),
     * devolvé la partida falsa".
     */
    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);

    // Ejecución
    servicioJuego.avanzarTurno(partidaId);

    /*
     * Mockito:
     *
     * verify(partida).avanzarTurno()
     *
     * Verifica que ServicioJuegoImpl haya llamado al método avanzarTurno()
     * de la partida.
     */
    verify(partida).avanzarTurno();

    /*
     * Mockito:
     *
     * Verifica que ServicioJuegoImpl haya actualizado la partida
     * después de avanzar el turno.
     */
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alRegistrarPreguntaHechaDebeActualizarLaPartida() {
    // Preparación
    Long partidaId = 1L;
    Long preguntaId = 10L;
    Partida partida = mock(Partida.class);

    /*
     * Mockito:
     *
     * Simulamos que existe una partida con ese id.
     */
    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);

    // Ejecución
    servicioJuego.registrarPreguntaHecha(partidaId, preguntaId);

    /*
     * Mockito:
     *
     * Verificamos que la partida registre la pregunta hecha.
     */
    verify(partida).registrarPreguntaHecha(preguntaId);

    /*
     * Mockito:
     *
     * Verificamos que luego se actualice la partida.
     */
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alValidarAtaqueDebeDelegarLaValidacionEnLaProvincia() {
    // Preparación
    Long jugadorId = 1L;
    Long provinciaId = 2L;
    Provincia provincia = mock(Provincia.class);

    /*
     * Mockito:
     *
     * Simulamos que el servicioProvincia encuentra una provincia.
     */
    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);

    // Ejecución
    servicioJuego.validarAtaque(jugadorId, provinciaId);

    /*
     * Mockito:
     *
     * Verificamos que ServicioJuegoImpl delegue la validación
     * en la entidad Provincia.
     */
    verify(provincia).validarAtaque(jugadorId);
  }

  @Test
  public void siLaProvinciaNoExisteNoDebeValidarAtaque() {
    // Preparación
    Long jugadorId = 1L;
    Long provinciaId = 2L;

    /*
     * Mockito:
     *
     * Simulamos que no se encontró ninguna provincia.
     */
    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(null);

    // Ejecución
    servicioJuego.validarAtaque(jugadorId, provinciaId);

    /*
     * Mockito:
     *
     * Verificamos que al menos se haya intentado buscar la provincia.
     *
     * Como la provincia es null, no hay un objeto provincia sobre el cual
     * verificar validarAtaque().
     */
    verify(servicioProvincia).buscarPorId(provinciaId);
  }

  @Test
  public void siNoEsTurnoDelJugadorDebeLanzarTurnoInvalidoException() {
    // Preparación
    Long partidaId = 1L;
    Long jugadorId = 2L;
    Long provinciaId = 3L;
    Partida partida = mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);

    /*
     * Mockito:
     *
     * Simulamos que NO es el turno del jugador.
     */
    when(partida.esTurnoDe(jugadorId)).thenReturn(false);

    /*
     * JUnit:
     *
     * assertThrows verifica que el código lance una excepción.
     *
     * En este caso esperamos TurnoInvalidoException.
     */
    assertThrows(
      TurnoInvalidoException.class,
      () -> servicioJuego.procesarJugada(partidaId, jugadorId, provinciaId)
    );

    /*
     * Mockito:
     *
     * never() verifica que NO se haya llamado al método actualizar().
     *
     * Si no era el turno del jugador, la partida no debería guardarse.
     */
    verify(servicioPartida, never()).actualizar(partida);
  }

  @Test
  public void siElTiempoEstaAgotadoDebeAvanzarTurnoYActualizarPartida() {
    // Preparación
    Long partidaId = 1L;
    Long jugadorId = 2L;
    Long provinciaId = 3L;
    Partida partida = mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.esTurnoDe(jugadorId)).thenReturn(true);

    /*
     * Mockito:
     *
     * ServicioJuegoImpl usa TIEMPO_MAXIMO_TURNO = 30.
     *
     * Simulamos que la partida tiene el tiempo agotado.
     */
    when(partida.tieneTiempoAgotado(30)).thenReturn(true);

    /*
     * JUnit:
     *
     * Esperamos que el servicio lance TiempoAgotadoException.
     */
    assertThrows(
      TiempoAgotadoException.class,
      () -> servicioJuego.procesarJugada(partidaId, jugadorId, provinciaId)
    );

    /*
     * Mockito:
     *
     * Si el tiempo se agotó, ServicioJuegoImpl debe:
     * 1. avanzar el turno
     * 2. actualizar la partida
     */
    verify(partida).avanzarTurno();
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void siLaJugadaEsValidaDebePasarALaEtapaDosYActualizarPartida()
    throws TiempoAgotadoException, TurnoInvalidoException {
    // Preparación
    Long partidaId = 1L;
    Long jugadorId = 2L;
    Long provinciaId = 3L;
    Partida partida = mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.esTurnoDe(jugadorId)).thenReturn(true);
    when(partida.tieneTiempoAgotado(30)).thenReturn(false);

    // Ejecución
    servicioJuego.procesarJugada(partidaId, jugadorId, provinciaId);

    /*
     * Mockito:
     *
     * Si la jugada es válida, el servicio debe pasar la partida
     * a etapa 2.
     */
    verify(partida).setEtapaActual(2);

    /*
     * Mockito:
     *
     * any(LocalDateTime.class) significa:
     * "acepto cualquier LocalDateTime".
     *
     * No nos interesa comprobar el segundo exacto.
     * Solo queremos verificar que se haya seteado una fecha/hora.
     */
    verify(partida).setInicioEtapa(any(LocalDateTime.class));

    /*
     * Mockito:
     *
     * Finalmente, debe guardar los cambios de la partida.
     */
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void siLaPartidaNoExisteNoDebeForzarSaltoPorTiempo() {
    // Preparación
    Long partidaId = 1L;

    /*
     * Mockito:
     *
     * Simulamos que no se encontró la partida.
     */
    when(servicioPartida.buscarPorId(partidaId)).thenReturn(null);

    // Ejecución
    servicioJuego.forzarSaltoPorTiempo(partidaId);

    /*
     * Mockito:
     *
     * Si la partida no existe, no debe actualizar nada.
     */
    verify(servicioPartida, never()).actualizar(any());
  }

  @Test
  public void siElTiempoEstaAgotadoDebeForzarSaltoDeTurno() {
    // Preparación
    Long partidaId = 1L;
    Partida partida = mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);

    /*
     * Mockito:
     *
     * En forzarSaltoPorTiempo(), ServicioJuegoImpl evalúa:
     *
     * TIEMPO_MAXIMO_TURNO - 2
     *
     * Como TIEMPO_MAXIMO_TURNO vale 30, entonces evalúa 28.
     */
    when(partida.tieneTiempoAgotado(28)).thenReturn(true);

    // Ejecución
    servicioJuego.forzarSaltoPorTiempo(partidaId);

    /*
     * Mockito:
     *
     * Verificamos que haya avanzado el turno y actualizado la partida.
     */
    verify(partida).avanzarTurno();
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void siNoAlcanzaLaCantidadRequeridaDeRespuestasDebeRetornarNull() {
    /*
     * Este test usa principalmente JUnit.
     *
     * No necesitamos mocks porque evaluarAcierto() corta al principio:
     *
     * if (respondidas < requeridas) {
     *   return null;
     * }
     */

    // Ejecución
    String resultado = servicioJuego.evaluarAcierto(1L, 2L, 1, 3);

    /*
     * JUnit:
     *
     * assertNull verifica que el resultado sea null.
     */
    assertNull(resultado);
  }

  @Test
  public void debeObtenerUsuarioPorId() {
    // Preparación
    Long usuarioId = 1L;
    Usuario usuario = mock(Usuario.class);

    /*
     * Mockito:
     *
     * Simulamos que servicioUsuario devuelve un usuario.
     */
    when(servicioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);

    // Ejecución
    Usuario resultado = servicioJuego.obtenerUsuarioPorId(usuarioId);

    /*
     * JUnit:
     *
     * assertSame verifica que sea exactamente el mismo objeto en memoria.
     *
     * No compara contenido.
     * Compara referencia.
     */
    assertSame(usuario, resultado);
  }

  @Test
  public void debeObtenerPreguntasHechasDeLaPartida() {
    // Preparación
    Long partidaId = 1L;
    Partida partida = mock(Partida.class);
    Set<Long> preguntasHechas = Set.of(1L, 2L, 3L);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getPreguntasHechas()).thenReturn(preguntasHechas);

    // Ejecución
    Set<Long> resultado = servicioJuego.obtenerPreguntasHechas(partidaId);

    /*
     * JUnit:
     *
     * Usamos assertSame porque queremos verificar que devuelve
     * el mismo Set que devolvió la partida mockeada.
     */
    assertSame(preguntasHechas, resultado);
  }

  @Test
  public void debeObtenerProvinciasDelTablero() {
    // Preparación
    List<Provincia> provincias = List.of(mock(Provincia.class), mock(Provincia.class));

    when(servicioProvincia.obtenerProvincias()).thenReturn(provincias);

    // Ejecución
    List<Provincia> resultado = servicioJuego.obtenerProvinciasDelTablero();

    /*
     * JUnit:
     *
     * Verificamos que devuelve exactamente la misma lista
     * que le entregó servicioProvincia.
     */
    assertSame(provincias, resultado);
  }

  @Test
  public void debeObtenerPartidaPorId() {
    // Preparación
    Long partidaId = 1L;
    Partida partida = mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);

    // Ejecución
    Partida resultado = servicioJuego.obtenerPartidaPorId(partidaId);

    /*
     * JUnit:
     *
     * Verificamos que devuelve la misma partida que obtuvo del servicio.
     */
    assertSame(partida, resultado);
  }

  @Test
  public void debeObtenerCantidadPreguntasRequeridas() {
    // Preparación
    Long provinciaId = 1L;

    when(servicioProvincia.obtenerCantidadPreguntasRequeridas(provinciaId)).thenReturn(3);

    // Ejecución
    Integer resultado = servicioJuego.obtenerCantidadPreguntasRequeridas(provinciaId);

    /*
     * JUnit:
     *
     * assertEquals compara valores.
     *
     * Para Integer, String, Long, etc., normalmente usamos assertEquals.
     */
    assertEquals(3, resultado);
  }

  @Test
  public void alActualizarPartidaDebeDelegarEnServicioPartida() {
    /*
     * Este test es simple.
     *
     * Queremos verificar que ServicioJuegoImpl.actualizarPartida()
     * no haga lógica propia, sino que delegue en ServicioPartida.
     */

    // Preparación
    Partida partida = org.mockito.Mockito.mock(Partida.class);

    // Ejecución
    servicioJuego.actualizarPartida(partida);

    // Verificación
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alConcretarColonizacionDebeSumarPuntosYAsignarProvincia()
    throws TiempoAgotadoException, TurnoInvalidoException {
    /*
     * Caso:
     *
     * El jugador coloniza una provincia.
     *
     * Reglas esperadas:
     * - suma 20 puntos
     * - la provincia pasa a valer 20 puntos
     * - la provincia queda asignada al jugador actual
     * - se registra una conquista en la partida
     * - se actualizan jugador, provincia y partida
     */

    // Preparación
    Long partidaId = 1L;
    Long provinciaId = 2L;
    Long jugadorId = 3L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorActual = org.mockito.Mockito.mock(Jugador.class);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorActual);
    when(jugadorActual.getId()).thenReturn(jugadorId);
    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);
    when(partida.alcanzoLimiteConquistas()).thenReturn(false);

    // Ejecución
    servicioJuego.concretarColonizacion(partidaId, provinciaId);

    // Verificación
    verify(jugadorActual).sumarPuntos(20);
    verify(provincia).setPuntos(20);
    verify(provincia).setIdJugadorDuenio(jugadorId);
    verify(partida).registrarConquista();

    /*
     * Como no alcanzó el límite de conquistas,
     * no debería avanzar el turno.
     */
    verify(partida, never()).avanzarTurno();

    verify(servicioJugador).actualizar(jugadorActual);
    verify(servicioProvincia).actualizar(provincia);
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alConcretarColonizacionYAlcanzarLimiteDebeAvanzarTurno() {
    /*
     * Este test prueba el otro camino de concretarColonizacion().
     *
     * Si luego de registrar la conquista se alcanza el límite,
     * la partida debe avanzar el turno.
     */

    // Preparación
    Long partidaId = 1L;
    Long provinciaId = 2L;
    Long jugadorId = 3L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorActual = org.mockito.Mockito.mock(Jugador.class);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorActual);
    when(jugadorActual.getId()).thenReturn(jugadorId);
    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);
    when(partida.alcanzoLimiteConquistas()).thenReturn(true);

    // Ejecución
    servicioJuego.concretarColonizacion(partidaId, provinciaId);

    // Verificación
    verify(partida).registrarConquista();
    verify(partida).avanzarTurno();
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alConcretarConquistaDebeSumarPuntosYAsignarProvincia() {
    /*
     * Caso:
     *
     * El jugador conquista una provincia.
     *
     * Reglas esperadas:
     * - suma 75 puntos
     * - la provincia pasa a valer 75 puntos
     * - la provincia queda asignada al jugador actual
     * - se registra la conquista
     * - se actualizan jugador, provincia y partida
     */

    // Preparación
    Long partidaId = 1L;
    Long provinciaId = 2L;
    Long jugadorId = 3L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorActual = org.mockito.Mockito.mock(Jugador.class);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorActual);
    when(jugadorActual.getId()).thenReturn(jugadorId);
    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);
    when(partida.alcanzoLimiteConquistas()).thenReturn(false);

    // Ejecución
    servicioJuego.concretarConquista(partidaId, provinciaId);

    // Verificación
    verify(jugadorActual).sumarPuntos(75);
    verify(provincia).setPuntos(75);
    verify(provincia).setIdJugadorDuenio(jugadorId);
    verify(partida).registrarConquista();
    verify(partida, never()).avanzarTurno();

    verify(servicioJugador).actualizar(jugadorActual);
    verify(servicioProvincia).actualizar(provincia);
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void alConcretarConquistaDebeRestarPuntosAlDuenioAnterior() {
    /*
     * Caso:
     *
     * La provincia ya tenía dueño.
     *
     * Regla esperada:
     * - al ex dueño se le restan 5 puntos
     * - se actualiza el ex dueño
     * - luego se asigna la provincia al jugador actual
     */

    // Preparación
    Long partidaId = 1L;
    Long provinciaId = 2L;
    Long jugadorActualId = 3L;
    Long exDuenioId = 99L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorActual = org.mockito.Mockito.mock(Jugador.class);
    Jugador exDuenio = org.mockito.Mockito.mock(Jugador.class);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorActual);
    when(jugadorActual.getId()).thenReturn(jugadorActualId);

    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);
    when(provincia.getIdJugadorDuenio()).thenReturn(exDuenioId);
    when(servicioJugador.buscarPorId(exDuenioId)).thenReturn(exDuenio);

    // Ejecución
    servicioJuego.concretarConquista(partidaId, provinciaId);

    // Verificación
    verify(exDuenio).restarPuntos(5);
    verify(servicioJugador).actualizar(exDuenio);

    verify(jugadorActual).sumarPuntos(75);
    verify(provincia).setIdJugadorDuenio(jugadorActualId);
    verify(servicioJugador).actualizar(jugadorActual);
  }

  @Test
  public void alIniciarAtaqueDebeValidarAtaqueYProcesarJugada()
    throws TiempoAgotadoException, TurnoInvalidoException {
    /*
     * iniciarAtaque() hace dos cosas:
     *
     * 1. Valida si la provincia puede ser atacada.
     * 2. Procesa la jugada.
     *
     * Para probarlo, simulamos una partida válida.
     */

    // Preparación
    Long partidaId = 1L;
    Long provinciaId = 2L;
    Long jugadorId = 3L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Jugador jugadorActual = org.mockito.Mockito.mock(Jugador.class);
    Provincia provincia = org.mockito.Mockito.mock(Provincia.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.getJugadorEnTurno()).thenReturn(jugadorActual);
    when(jugadorActual.getId()).thenReturn(jugadorId);

    when(servicioProvincia.buscarPorId(provinciaId)).thenReturn(provincia);

    when(partida.esTurnoDe(jugadorId)).thenReturn(true);
    when(partida.tieneTiempoAgotado(30)).thenReturn(false);

    // Ejecución
    servicioJuego.iniciarAtaque(partidaId, provinciaId);

    // Verificación
    verify(provincia).validarAtaque(jugadorId);
    verify(partida).setEtapaActual(2);
    verify(partida).setInicioEtapa(any(java.time.LocalDateTime.class));
    verify(servicioPartida).actualizar(partida);
  }

  @Test
  public void siLaPartidaNoEstaFinalizadaEvaluarYFinalizarDebeRetornarFalse() {
    /*
     * Si la partida existe pero no está finalizada,
     * evaluarYFinalizarPartida() debe devolver false.
     */

    // Preparación
    Long partidaId = 1L;
    Long usuarioId = 2L;
    Partida partida = org.mockito.Mockito.mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.estaFinalizada()).thenReturn(false);

    // Ejecución
    boolean resultado = servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId);

    // Verificación
    assertFalse(resultado);
    verify(servicioUsuario, never()).actualizarUsuario(any(Usuario.class));
    verify(servicioHistorial, never()).guardar(any(HistorialPartida.class));
  }

  @Test
  public void siLaPartidaEstaFinalizadaDebeRegistrarResultadoYRetornarTrue() {
    /*
     * Caso:
     *
     * La partida ya finalizó.
     *
     * Reglas esperadas:
     * - se busca el usuario
     * - se calcula el puesto final
     * - se registra el fin de partida en el usuario
     * - se actualiza el usuario
     * - se guarda un historial
     * - devuelve true
     */

    // Preparación
    Long partidaId = 1L;
    Long usuarioId = 2L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);
    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.estaFinalizada()).thenReturn(true);

    when(servicioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(usuario);
    when(partida.calcularPuestoDeUsuario(usuarioId)).thenReturn(1);
    when(usuario.registrarFinDePartida(1)).thenReturn(100);
    when(partida.obtenerNombreGanador()).thenReturn("Ganador");

    // Ejecución
    boolean resultado = servicioJuego.evaluarYFinalizarPartida(partidaId, usuarioId);

    // Verificación
    assertTrue(resultado);

    verify(usuario).registrarFinDePartida(1);
    verify(servicioUsuario).actualizarUsuario(usuario);
    verify(servicioHistorial).guardar(any(HistorialPartida.class));
  }

  @Test
  public void siLaPartidaNoExisteAlFinalizarDebeLanzarIllegalStateException() {
    /*
     * finalizarYRegistrarPartida() requiere que la partida exista
     * y esté finalizada.
     *
     * Si no existe, debe lanzar IllegalStateException.
     */

    // Preparación
    Long partidaId = 1L;
    Long usuarioId = 2L;

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalStateException.class,
      () -> servicioJuego.finalizarYRegistrarPartida(partidaId, usuarioId)
    );
  }

  @Test
  public void siElUsuarioNoExisteAlFinalizarDebeLanzarIllegalArgumentException() {
    /*
     * Caso:
     *
     * La partida existe y está finalizada,
     * pero el usuario anfitrión no existe.
     *
     * Debe lanzar IllegalArgumentException.
     */

    // Preparación
    Long partidaId = 1L;
    Long usuarioId = 2L;

    Partida partida = org.mockito.Mockito.mock(Partida.class);

    when(servicioPartida.buscarPorId(partidaId)).thenReturn(partida);
    when(partida.estaFinalizada()).thenReturn(true);
    when(servicioUsuario.buscarUsuarioPorId(usuarioId)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioJuego.finalizarYRegistrarPartida(partidaId, usuarioId)
    );
  }
}
