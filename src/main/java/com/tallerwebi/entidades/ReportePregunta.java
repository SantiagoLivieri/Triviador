package com.tallerwebi.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@Table(
  name = "ReportePregunta",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_reporte_respuesta_partida", columnNames = "respuesta_partida_id"),
  }
)
@Getter
@NoArgsConstructor
public class ReportePregunta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "respuesta_partida_id", nullable = false)
  private RespuestaPartida respuestaPartida;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private MotivoReportePregunta motivo;

  @Column(length = 1000)
  private String comentario;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private EstadoReportePregunta estado;

  @Column(name = "fecha_reporte", nullable = false)
  private LocalDateTime fechaReporte;

  public ReportePregunta(
    RespuestaPartida respuestaPartida,
    Usuario usuario,
    MotivoReportePregunta motivo,
    String comentario
  ) {
    this.respuestaPartida = respuestaPartida;
    this.usuario = usuario;
    this.motivo = motivo;
    this.comentario = comentario;
    this.estado = EstadoReportePregunta.PENDIENTE;
    this.fechaReporte = LocalDateTime.now();
  }

  public Partida obtenerPartida() {
    return respuestaPartida.getPartida();
  }

  public Pregunta obtenerPregunta() {
    return respuestaPartida.getPregunta();
  }

  public void cambiarEstado(EstadoReportePregunta nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado del reporte es obligatorio.");
    }

    this.estado = nuevoEstado;
  }
}
