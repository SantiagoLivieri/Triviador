package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("PMD.TooManyFields")
@Entity
@Table(
  name = "RespuestaPartida",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_respuesta_partida_jugador_pregunta",
      columnNames = { "partida_id", "jugador_id", "pregunta_id" }
    ),
  }
)
@Getter
@NoArgsConstructor
public class RespuestaPartida {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "partida_id", nullable = false)
  private Partida partida;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pregunta_id", nullable = false)
  private Pregunta pregunta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "jugador_id", nullable = false)
  private Jugador jugador;

  /*
   * Puede ser null para jugadores invitados del modo local.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  /*
   * Guardamos una copia del texto para que el historial no cambie
   * si posteriormente un editor modifica la pregunta.
   */
  @Column(name = "enunciado_pregunta", nullable = false, length = 500)
  private String enunciadoPregunta;

  @Column(name = "respuesta_elegida", nullable = false, length = 255)
  private String respuestaElegida;

  @Column(name = "respuesta_correcta", nullable = false, length = 255)
  private String respuestaCorrecta;

  @Column(nullable = false)
  private boolean correcta;

  @Column(name = "cantidad_intentos", nullable = false)
  private Integer cantidadIntentos;

  @Column(name = "fecha_respuesta", nullable = false)
  private LocalDateTime fechaRespuesta;

  @OneToOne(mappedBy = "respuestaPartida", fetch = FetchType.LAZY)
  private ReportePregunta reporte;

  public RespuestaPartida(
    Partida partida,
    Pregunta pregunta,
    Jugador jugador,
    Usuario usuario,
    String respuestaElegida,
    boolean correcta
  ) {
    this.partida = partida;
    this.pregunta = pregunta;
    this.jugador = jugador;
    this.usuario = usuario;
    this.enunciadoPregunta = pregunta.getEnunciado();
    this.respuestaElegida = respuestaElegida;
    this.respuestaCorrecta = pregunta.getRespuestaCorrecta();
    this.correcta = correcta;
    this.cantidadIntentos = 1;
    this.fechaRespuesta = LocalDateTime.now();
  }

  /*
   * Se utiliza cuando la misma pregunta recibe un segundo intento
   * mediante el comodín Doble Chance.
   */
  public void actualizarRespuesta(String nuevaRespuesta, boolean nuevaRespuestaCorrecta) {
    this.respuestaElegida = nuevaRespuesta;
    this.correcta = nuevaRespuestaCorrecta;
    this.cantidadIntentos++;
    this.fechaRespuesta = LocalDateTime.now();
  }

  /*
   * Evita incrementar intentos si llega dos veces exactamente
   * el mismo POST.
   */
  public boolean coincideCon(String respuesta, boolean resultadoCorrecto) {
    return (Objects.equals(this.respuestaElegida, respuesta) && this.correcta == resultadoCorrecto);
  }
}
