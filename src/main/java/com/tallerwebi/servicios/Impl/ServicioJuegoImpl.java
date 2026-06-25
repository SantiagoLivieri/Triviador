package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.DatosLobby;
import com.tallerwebi.entidades.HistorialPartida;
import com.tallerwebi.entidades.Jugador;
import com.tallerwebi.entidades.Partida;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioHistorial;
import com.tallerwebi.servicios.ServicioJuego;
import com.tallerwebi.servicios.ServicioJugador;
import com.tallerwebi.servicios.ServicioPartida;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.ServicioUsuario;
import com.tallerwebi.servicios.excepcion.TiempoAgotadoException;
import com.tallerwebi.servicios.excepcion.TurnoInvalidoException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

  private final ServicioJugador servicioJugador;
  private final ServicioProvincia servicioProvincia;
  private final ServicioPartida servicioPartida;
  private final ServicioHistorial servicioHistorial;
  private final ServicioUsuario servicioUsuario;
  private static final int TIEMPO_MAXIMO_TURNO = 30;
  private static final int REQUERIDAS_POR_CONQUISTA = 3;

  public ServicioJuegoImpl(
    ServicioJugador servicioJugador,
    ServicioProvincia servicioProvincia,
    ServicioPartida servicioPartida,
    ServicioHistorial servicioHistorial,
    ServicioUsuario servicioUsuario
  ) {
    this.servicioJugador = servicioJugador;
    this.servicioProvincia = servicioProvincia;
    this.servicioPartida = servicioPartida;
    this.servicioHistorial = servicioHistorial;
    this.servicioUsuario = servicioUsuario;
  }

  @Override
  public Long inicializarPartida(DatosLobby datosLobby, Usuario usuarioAnfitrion) {
    servicioProvincia.resetearProvincias();

    List<Jugador> jugadores = new ArrayList<>(
      List.of(
        servicioJugador.crearJugadorConUsuario(usuarioAnfitrion, datosLobby.getColorJugadorUno()),
        servicioJugador.crearJugador(
          datosLobby.getNombreJugadorDos(),
          datosLobby.getColorJugadorDos()
        ),
        servicioJugador.crearJugador(
          datosLobby.getNombreJugadorTres(),
          datosLobby.getColorJugadorTres()
        )
      )
    );

    Partida partida = servicioPartida.crearPartida(jugadores);

    return partida.getId();
  }

  // Se ocupa de cambio de dueño y puntaje en conquistas
  @Override
  public void concretarConquista(Long partidaId, Long idProvincia) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    Jugador jugadorActual = partida.getJugadorEnTurno();
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    Jugador exduenio = servicioJugador.buscarPorId(provincia.getIdJugadorDuenio());

    if (exduenio != null) {
      exduenio.restarPuntos(5);
      servicioJugador.actualizar(exduenio);
    }

    jugadorActual.sumarPuntos(75);
    provincia.setPuntos(75);
    provincia.setIdJugadorDuenio(jugadorActual.getId());

    partida.registrarConquista();
    if (partida.alcanzoLimiteConquistas()) {
      partida.avanzarTurno();
    }

    servicioJugador.actualizar(jugadorActual);
    servicioProvincia.actualizar(provincia);
    servicioPartida.actualizar(partida);
  }

  @Override
  public void concretarColonizacion(Long partidaId, Long idProvincia) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    Jugador jugadorActual = partida.getJugadorEnTurno();
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    jugadorActual.sumarPuntos(20);
    provincia.setPuntos(20);
    provincia.setIdJugadorDuenio(jugadorActual.getId());

    partida.registrarConquista();
    if (partida.alcanzoLimiteConquistas()) {
      partida.avanzarTurno();
    }

    servicioJugador.actualizar(jugadorActual);
    servicioProvincia.actualizar(provincia);
    servicioPartida.actualizar(partida);
  }

  @Override
  public void avanzarTurno(Long partidaId) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    partida.avanzarTurno();
    servicioPartida.actualizar(partida);
  }

  @Override
  public void registrarPreguntaHecha(Long partidaId, Long idPregunta) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    partida.registrarPreguntaHecha(idPregunta);
    servicioPartida.actualizar(partida);
  }

  @Override
  public void iniciarAtaque(Long partidaId, Long idProvincia)
    throws TiempoAgotadoException, TurnoInvalidoException {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    Long jugadorId = partida.getJugadorEnTurno().getId();

    this.validarAtaque(jugadorId, idProvincia);

    this.procesarJugada(partidaId, jugadorId, idProvincia);
  }

  @Override
  public void validarAtaque(Long jugadorId, Long idProvincia) {
    Provincia provincia = servicioProvincia.buscarPorId(idProvincia);

    if (provincia != null) {
      provincia.validarAtaque(jugadorId);
    }
  }

  @Override
  public void procesarJugada(Long partidaId, Long jugadorId, Long provinciaSeleccionadaId)
    throws TiempoAgotadoException, TurnoInvalidoException {
    Partida partida = servicioPartida.buscarPorId(partidaId);

    if (!partida.esTurnoDe(jugadorId)) {
      throw new TurnoInvalidoException("No es tu turno");
    }

    if (partida.tieneTiempoAgotado(TIEMPO_MAXIMO_TURNO)) {
      partida.avanzarTurno();
      servicioPartida.actualizar(partida);
      throw new TiempoAgotadoException("Se te acabó el tiempo");
    }

    partida.setEtapaActual(2);
    partida.setInicioEtapa(LocalDateTime.now());
    servicioPartida.actualizar(partida);
  }

  @Override
  public void forzarSaltoPorTiempo(Long partidaId) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    if (partida == null) return;

    if (partida.tieneTiempoAgotado(TIEMPO_MAXIMO_TURNO - 2)) {
      partida.avanzarTurno();
      servicioPartida.actualizar(partida);
    }
  }

  @Override
  public String evaluarAcierto(
    Long partidaId,
    Long idProvincia,
    Integer respondidas,
    Integer requeridas
  ) {
    if (respondidas < requeridas) {
      return null;
    }

    Partida partida = servicioPartida.buscarPorId(partidaId);
    Jugador jugadorQueRespondio = partida.getJugadorEnTurno();

    String mensajeFinal;
    if (requeridas == REQUERIDAS_POR_CONQUISTA) {
      this.concretarConquista(partidaId, idProvincia);
      mensajeFinal = "¡Respondiste las 3 correctas y conquistaste la provincia!";
    } else {
      this.concretarColonizacion(partidaId, idProvincia);
      mensajeFinal = "¡Respuesta correcta! Provincia colonizada.";
    }

    Partida partidaDespues = servicioPartida.buscarPorId(partidaId);
    if (!jugadorQueRespondio.getId().equals(partidaDespues.getJugadorEnTurno().getId())) {
      mensajeFinal += "\n\nAlcanzaste el límite máximo de 3 conquistas. Fin de tu turno.";
    }

    return mensajeFinal;
  }

  @Override
  public boolean evaluarYFinalizarPartida(Long partidaId, Long usuarioId) {
    Partida partida = servicioPartida.buscarPorId(partidaId);

    if (partida != null && partida.estaFinalizada()) {
      this.finalizarYRegistrarPartida(partidaId, usuarioId);
      return true;
    }

    return false;
  }

  @Override
  public void finalizarYRegistrarPartida(Long partidaId, Long usuarioId) {
    Partida partida = servicioPartida.buscarPorId(partidaId);
    if (partida == null || !partida.estaFinalizada()) {
      throw new IllegalStateException("La partida no ha finalizado o no existe.");
    }

    Usuario usuario = servicioUsuario.buscarUsuarioPorId(usuarioId);
    if (usuario == null) {
      throw new IllegalArgumentException("El usuario anfitrión no existe.");
    }

    int puestoFinal = partida.calcularPuestoDeUsuario(usuarioId);
    int xpGanada = usuario.registrarFinDePartida(puestoFinal);
    String nombreGanador = partida.obtenerNombreGanador();

    servicioUsuario.actualizarUsuario(usuario);

    HistorialPartida ticketHistorial = new HistorialPartida(
      usuario,
      puestoFinal,
      xpGanada,
      nombreGanador
    );
    servicioHistorial.guardar(ticketHistorial);
  }

  @Override
  public void actualizarPartida(Partida partida) {
    servicioPartida.actualizar(partida);
  }

  @Override
  public Usuario obtenerUsuarioPorId(Long usuarioId) {
    return this.servicioUsuario.buscarUsuarioPorId(usuarioId);
  }

  @Override
  public Set<Long> obtenerPreguntasHechas(Long partidaId) {
    return servicioPartida.buscarPorId(partidaId).getPreguntasHechas();
  }

  @Override
  public List<Provincia> obtenerProvinciasDelTablero() {
    return servicioProvincia.obtenerProvincias();
  }

  @Override
  public Partida obtenerPartidaPorId(Long partidaId) {
    return servicioPartida.buscarPorId(partidaId);
  }

  @Override
  public Integer obtenerCantidadPreguntasRequeridas(Long idProvincia) {
    return servicioProvincia.obtenerCantidadPreguntasRequeridas(idProvincia);
  }
}
