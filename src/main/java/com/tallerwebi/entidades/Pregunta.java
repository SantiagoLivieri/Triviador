package com.tallerwebi.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pregunta")
@Getter
@Setter
@NoArgsConstructor
public class Pregunta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 500)
  private String enunciado;

  @Column(name = "respuesta_correcta", nullable = false, length = 255)
  private String respuestaCorrecta;

  @Column(name = "opcion_incorrecta_uno", nullable = false, length = 255)
  private String opcionIncorrectaUno;

  @Column(name = "opcion_incorrecta_dos", nullable = false, length = 255)
  private String opcionIncorrectaDos;

  @Column(name = "opcion_incorrecta_tres", nullable = false, length = 255)
  private String opcionIncorrectaTres;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_pregunta", nullable = false)
  private TipoPregunta tipoPregunta;

  @Enumerated(EnumType.STRING)
  @Column(name = "categoria_pregunta", length = 50)
  private CategoriaPregunta categoriaPregunta;

  public Pregunta(
    String enunciado,
    String respuestaCorrecta,
    String opcionIncorrectaUno,
    String opcionIncorrectaDos,
    String opcionIncorrectaTres,
    TipoPregunta tipoPregunta,
    CategoriaPregunta categoriaPregunta
  ) {
    this.enunciado = enunciado;
    this.respuestaCorrecta = respuestaCorrecta;
    this.opcionIncorrectaUno = opcionIncorrectaUno;
    this.opcionIncorrectaDos = opcionIncorrectaDos;
    this.opcionIncorrectaTres = opcionIncorrectaTres;
    this.tipoPregunta = tipoPregunta;
    this.categoriaPregunta = categoriaPregunta;
  }
}
