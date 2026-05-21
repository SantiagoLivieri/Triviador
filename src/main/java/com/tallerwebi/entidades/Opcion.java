package com.tallerwebi.entidades;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Opcion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Pregunta pregunta;

  private String descripcion;
  private Boolean esCorrecta;

  public Opcion(Long id, String descripcion, Boolean esCorrecta) {
    this.id = id;
    this.descripcion = descripcion;
    this.esCorrecta = esCorrecta;
  }
}
