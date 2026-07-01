package com.tallerwebi.servicios.Impl;

import com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta;
import com.tallerwebi.entidades.EstadoSugerenciaPregunta;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.entidades.TipoPregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.repositorios.RepositorioSugerenciaPregunta;
import com.tallerwebi.servicios.ServicioSugerenciaPregunta;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioSugerenciaPreguntaImpl implements ServicioSugerenciaPregunta {

  private final RepositorioSugerenciaPregunta repositorioSugerenciaPregunta;
  private final RepositorioPregunta repositorioPregunta;
  private final RepositorioProvincia repositorioProvincia;

  @Autowired
  public ServicioSugerenciaPreguntaImpl(
    RepositorioSugerenciaPregunta repositorioSugerenciaPregunta,
    RepositorioPregunta repositorioPregunta,
    RepositorioProvincia repositorioProvincia
  ) {
    this.repositorioSugerenciaPregunta = repositorioSugerenciaPregunta;
    this.repositorioPregunta = repositorioPregunta;
    this.repositorioProvincia = repositorioProvincia;
  }

  @Override
  public void crearSugerencia(DatosSugerenciaPregunta datos, Usuario usuarioCreador) {
    validarUsuarioJugador(usuarioCreador);
    validarDatos(datos);

    Provincia provincia = repositorioProvincia.buscarPorId(datos.getIdProvincia());

    if (provincia == null) {
      throw new IllegalArgumentException("La provincia seleccionada no existe.");
    }

    SugerenciaPregunta sugerencia = new SugerenciaPregunta(
      datos.getEnunciado().trim(),
      datos.getRespuestaCorrecta().trim(),
      datos.getOpcionIncorrectaUno().trim(),
      datos.getOpcionIncorrectaDos().trim(),
      datos.getOpcionIncorrectaTres().trim(),
      provincia,
      usuarioCreador
    );

    repositorioSugerenciaPregunta.guardar(sugerencia);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SugerenciaPregunta> obtenerSugerenciasPendientes() {
    return repositorioSugerenciaPregunta.buscarPorEstado(EstadoSugerenciaPregunta.PENDIENTE);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SugerenciaPregunta> obtenerTodas() {
    return repositorioSugerenciaPregunta.buscarTodas();
  }

  @Override
  @Transactional(readOnly = true)
  public SugerenciaPregunta buscarPorId(Long id) {
    return repositorioSugerenciaPregunta.buscarPorId(id);
  }

  @Override
  public void aprobarSugerencia(Long idSugerencia, Usuario usuarioAdmin) {
    validarUsuarioAdmin(usuarioAdmin);

    SugerenciaPregunta sugerencia = repositorioSugerenciaPregunta.buscarPorId(idSugerencia);

    if (sugerencia == null) {
      throw new IllegalArgumentException("La sugerencia no existe.");
    }

    Pregunta preguntaAprobada = new Pregunta(
      sugerencia.getEnunciado(),
      sugerencia.getRespuestaCorrecta(),
      sugerencia.getOpcionIncorrectaUno(),
      sugerencia.getOpcionIncorrectaDos(),
      sugerencia.getOpcionIncorrectaTres(),
      TipoPregunta.MULTIPLE_CHOICE,
      null,
      sugerencia.getProvincia()
    );

    repositorioPregunta.guardar(preguntaAprobada);

    sugerencia.aprobar();
    repositorioSugerenciaPregunta.actualizar(sugerencia);
  }

  @Override
  public void actualizarSugerencia(DatosSugerenciaPregunta datos, Usuario usuarioAdmin) {
    validarUsuarioAdmin(usuarioAdmin);

    if (datos == null || datos.getId() == null) {
      throw new IllegalArgumentException("No se encontró la sugerencia a editar.");
    }

    validarDatos(datos);

    SugerenciaPregunta sugerencia = repositorioSugerenciaPregunta.buscarPorId(datos.getId());

    if (sugerencia == null) {
      throw new IllegalArgumentException("La sugerencia no existe.");
    }

    Provincia provincia = repositorioProvincia.buscarPorId(datos.getIdProvincia());

    if (provincia == null) {
      throw new IllegalArgumentException("La provincia seleccionada no existe.");
    }

    sugerencia.actualizarDatos(
      datos.getEnunciado().trim(),
      datos.getRespuestaCorrecta().trim(),
      datos.getOpcionIncorrectaUno().trim(),
      datos.getOpcionIncorrectaDos().trim(),
      datos.getOpcionIncorrectaTres().trim(),
      provincia
    );

    repositorioSugerenciaPregunta.actualizar(sugerencia);
  }

  @Override
  public void eliminarSugerencia(Long idSugerencia, Usuario usuarioAdmin) {
    validarUsuarioAdmin(usuarioAdmin);

    SugerenciaPregunta sugerencia = repositorioSugerenciaPregunta.buscarPorId(idSugerencia);

    if (sugerencia == null) {
      throw new IllegalArgumentException("La sugerencia no existe.");
    }

    repositorioSugerenciaPregunta.eliminar(sugerencia);
  }

  private void validarDatos(DatosSugerenciaPregunta datos) {
    if (datos == null) {
      throw new IllegalArgumentException("Los datos de la sugerencia son obligatorios.");
    }

    validarCampoObligatorio(datos.getEnunciado(), "El enunciado no puede estar vacío.");
    validarCampoObligatorio(
      datos.getRespuestaCorrecta(),
      "La respuesta correcta no puede estar vacía."
    );
    validarCampoObligatorio(
      datos.getOpcionIncorrectaUno(),
      "La primera respuesta incorrecta no puede estar vacía."
    );
    validarCampoObligatorio(
      datos.getOpcionIncorrectaDos(),
      "La segunda respuesta incorrecta no puede estar vacía."
    );
    validarCampoObligatorio(
      datos.getOpcionIncorrectaTres(),
      "La tercera respuesta incorrecta no puede estar vacía."
    );

    validarProvinciaSeleccionada(datos);
  }

  private void validarCampoObligatorio(String valor, String mensajeError) {
    if (estaVacio(valor)) {
      throw new IllegalArgumentException(mensajeError);
    }
  }

  private void validarProvinciaSeleccionada(DatosSugerenciaPregunta datos) {
    if (datos.getIdProvincia() == null) {
      throw new IllegalArgumentException("Debe seleccionar una provincia.");
    }
  }

  private void validarUsuarioJugador(Usuario usuario) {
    if (!tieneRol(usuario, "JUGADOR")) {
      throw new IllegalArgumentException(
        "Solo los usuarios con rol JUGADOR pueden sugerir preguntas."
      );
    }
  }

  private void validarUsuarioAdmin(Usuario usuario) {
    if (!tieneRol(usuario, "ADMIN")) {
      throw new IllegalArgumentException(
        "Solo los usuarios con rol ADMIN pueden administrar sugerencias."
      );
    }
  }

  private boolean tieneRol(Usuario usuario, String rolEsperado) {
    return (
      usuario != null &&
      usuario.getRol() != null &&
      usuario.getRol().getDescripcion() != null &&
      usuario.getRol().getDescripcion().equals(rolEsperado)
    );
  }

  private boolean estaVacio(String texto) {
    return texto == null || texto.trim().isEmpty();
  }
}
