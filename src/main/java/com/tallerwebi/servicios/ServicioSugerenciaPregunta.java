package com.tallerwebi.servicios;

import com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta;
import com.tallerwebi.entidades.SugerenciaPregunta;
import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface ServicioSugerenciaPregunta {
  void crearSugerencia(DatosSugerenciaPregunta datos, Usuario usuarioCreador);

  void crearPreguntaComoAdmin(DatosSugerenciaPregunta datos, Usuario usuarioAdmin);

  List<SugerenciaPregunta> obtenerSugerenciasPendientes();

  List<SugerenciaPregunta> obtenerTodas();

  SugerenciaPregunta buscarPorId(Long id);

  void aprobarSugerencia(Long idSugerencia, Usuario usuarioAdmin);

  void actualizarSugerencia(DatosSugerenciaPregunta datos, Usuario usuarioAdmin);

  void eliminarSugerencia(Long idSugerencia, Usuario usuarioAdmin);
}
