package com.tallerwebi.servicios.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.servicios.ServicioGemini;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioGeminiImpl implements ServicioGemini {

  @Value("${GEMINI_API_KEY:default}")
  private String apiKey;

  @Value("${GEMINI_MODEL:gemini-2.0-flash-lite}")
  private String modelo;

  private final RestTemplate restTemplate;

  private String systemInstruction = "";

  private static final String URL_BASE = "https://generativelanguage.googleapis.com/v1beta/models/";
  private static final String DEFAULT_API_KEY = "default";
  private static final String MODELO_FALLBACK = "gemini-2.0-flash-lite";
  private static final String MODELO_ALTERNATIVO = "gemini-2.0-flash";
  private static final int REINTENTOS_MAX = 2;
  private static final long ESPERA_REINTENTOS_MS = 3000;

  @Autowired
  public ServicioGeminiImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public void configurar(String systemInstruction) {
    this.systemInstruction = systemInstruction;
  }

  @Override
  public String getSystemInstruction() {
    return this.systemInstruction;
  }

  @Override
  public void setSystemInstruction(String Instruction) {
    if (this.systemInstruction == null || this.systemInstruction.isEmpty()) {
      this.systemInstruction = Instruction;
    } else {
      this.systemInstruction += " " + Instruction;
    }
  }

  @Override
  public void limpiarContexto() {
    this.systemInstruction = "";
  }

  @Override
  public String preguntar(String mensajeUsuario, String reglaAdicional, boolean persistir)
    throws JsonProcessingException {
    if (reglaAdicional != null && !reglaAdicional.isEmpty()) {
      if (persistir) {
        appendSystemInstruction(reglaAdicional);
      } else {
        return ejecutarConContexto(
          mensajeUsuario,
          (this.systemInstruction.isEmpty())
            ? reglaAdicional
            : this.systemInstruction + " " + reglaAdicional
        );
      }
    }
    return ejecutarConContexto(mensajeUsuario, this.systemInstruction);
  }

  private void appendSystemInstruction(String instruction) {
    if (instruction == null || instruction.isEmpty()) {
      return;
    }

    if (this.systemInstruction == null || this.systemInstruction.isEmpty()) {
      this.systemInstruction = instruction;
    } else {
      this.systemInstruction += " " + instruction;
    }
  }

  private String ejecutarConContexto(String mensaje, String contexto)
    throws JsonProcessingException {
    validarApiKey();

    String[] modelos = { this.modelo, MODELO_ALTERNATIVO, MODELO_FALLBACK };
    for (String modeloActual : modelos) {
      try {
        return ejecutarConModelo(mensaje, contexto, modeloActual);
      } catch (HttpClientErrorException.NotFound exception) {
        if (esUltimoModelo(modeloActual, modelos)) {
          throw exception;
        }
      }
    }
    throw new IllegalStateException("No fue posible contactar a Gemini.");
  }

  private void validarApiKey() {
    if (
      this.apiKey == null || this.apiKey.isBlank() || DEFAULT_API_KEY.equalsIgnoreCase(this.apiKey)
    ) {
      throw new IllegalStateException("Falta configurar GEMINI_API_KEY.");
    }
  }

  private boolean esUltimoModelo(String modeloActual, String... modelos) {
    return modeloActual.equals(modelos[modelos.length - 1]);
  }

  private String ejecutarConModelo(String mensaje, String contexto, String modeloActual)
    throws JsonProcessingException {
    int reintentos = 0;
    while (reintentos <= REINTENTOS_MAX) {
      try {
        return intentarLlamadaGemini(mensaje, contexto, modeloActual);
      } catch (HttpClientErrorException.TooManyRequests exception) {
        if (reintentos < REINTENTOS_MAX) {
          reintentos++;
          esperarAntesDReintentar();
        } else {
          throw new IllegalStateException(
            "La IA está saturada. Probá nuevamente en unos momentos.",
            exception
          );
        }
      }
    }
    return null;
  }

  private String intentarLlamadaGemini(String mensaje, String contexto, String modeloActual)
    throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-goog-api-key", this.apiKey);

    Map<String, Object> body = new HashMap<>();

    if (contexto != null && !contexto.isEmpty()) {
      Map<String, Object> systemInstructionPart = new HashMap<>();
      systemInstructionPart.put("parts", List.of(Map.of("text", contexto)));
      body.put("system_instruction", systemInstructionPart);
    }

    Map<String, Object> contents = new HashMap<>();
    Map<String, String> part = new HashMap<>();
    part.put("text", mensaje);
    contents.put("parts", List.of(part));
    body.put("contents", List.of(contents));

    ObjectMapper mapper = new ObjectMapper();
    String requestBody = mapper.writeValueAsString(body);

    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    String url = URL_BASE + modeloActual + ":generateContent";
    String response = restTemplate.postForObject(url, request, String.class);

    return extraerRespuesta(response);
  }

  private void esperarAntesDReintentar() {
    try {
      Thread.sleep(ESPERA_REINTENTOS_MS);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
    }
  }

  private String extraerRespuesta(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(json);
      JsonNode texto = root
        .path("candidates")
        .path(0)
        .path("content")
        .path("parts")
        .path(0)
        .path("text");

      if (texto.isMissingNode() || texto.asText().isBlank()) {
        throw new IllegalStateException("Gemini no devolvió texto en la respuesta.");
      }

      return texto.asText();
    } catch (Exception e) {
      throw new IllegalStateException("Error procesando respuesta de Gemini: " + e.getMessage(), e);
    }
  }
}
