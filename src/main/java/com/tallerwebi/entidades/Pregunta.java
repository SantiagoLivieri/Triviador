package com.tallerwebi.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

  @ManyToOne
  private Provincia provincia;

  public Provincia getProvincia() {
    return provincia;
  }

  public void setProvincia(Provincia provincia) {
    this.provincia = provincia;
  }

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
    CategoriaPregunta categoriaPregunta,
    Provincia provincia
  ) {
    this.enunciado = enunciado;
    this.respuestaCorrecta = respuestaCorrecta;
    this.opcionIncorrectaUno = opcionIncorrectaUno;
    this.opcionIncorrectaDos = opcionIncorrectaDos;
    this.opcionIncorrectaTres = opcionIncorrectaTres;
    this.tipoPregunta = tipoPregunta;
    this.categoriaPregunta = categoriaPregunta;
    this.provincia = provincia;
  }

  public Pregunta(
    String enunciado2,
    String respuestaCorrecta2,
    TipoPregunta tipo,
    CategoriaPregunta cat,
    Provincia prov
  ) {
    this.enunciado = enunciado2;
    this.respuestaCorrecta = respuestaCorrecta2;
    this.tipoPregunta = tipo;
    this.categoriaPregunta = cat;
    this.provincia = prov;
    //TODO Auto-generated constructor stub
  }

  public List<String> getOpcionesMezcladas() {
    List<String> opciones = new ArrayList<>();
    opciones.add(this.respuestaCorrecta);
    opciones.add(this.opcionIncorrectaUno);
    opciones.add(this.opcionIncorrectaDos);
    opciones.add(this.opcionIncorrectaTres);

    Collections.shuffle(opciones);
    return opciones;
  }
}
