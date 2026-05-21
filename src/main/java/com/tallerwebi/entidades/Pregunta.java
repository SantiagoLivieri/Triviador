package com.tallerwebi.entidades;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pregunta {

  @Enumerated(EnumType.STRING)
  private TipoPregunta tipoPregunta;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String enunciado;
}
