package com.tallerwebi.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ServicioGemini {
  void configurar(String systemInstruction);

  String getSystemInstruction();

  void setSystemInstruction(String instruction);

  void limpiarContexto();

  String preguntar(String mensajeUsuario, String reglaAdicional, boolean persistir)
    throws JsonProcessingException;
}
