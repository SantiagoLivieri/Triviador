package com.tallerwebi.controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.controladores.clasesAuxiliares.PreguntaGeneradaIA;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.servicios.ServicioEntrenamiento;
import com.tallerwebi.servicios.ServicioGemini;
import com.tallerwebi.servicios.ServicioPregunta;
import com.tallerwebi.servicios.ServicioProvincia;
import com.tallerwebi.servicios.ServicioSugerenciaPregunta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/entrenamiento")
public class ControladorEntrenamiento {

  private static final String ATRIBUTO_PREGUNTA_ACTUAL = "entrenamientoPreguntaActual";
  private static final String ATRIBUTO_PUNTAJE = "entrenamientoPuntaje";
  private static final String ATRIBUTO_RESPUESTAS = "entrenamientoRespuestas";
  private static final String ATRIBUTO_USADAS = "entrenamientoPreguntasUsadas";
  private static final String ATRIBUTO_FINALIZADO = "entrenamientoFinalizado";
  private static final String ATRIBUTO_MENSAJE = "entrenamientoMensaje";
  private static final String ATRIBUTO_PROVINCIA = "entrenamientoProvinciaSeleccionada";
  private static final String ATRIBUTO_USUARIO = "usuarioLogueado";
  private static final String ATRIBUTO_PREGUNTA_GENERADA = "preguntaGeneradaIA";
  private static final String ATRIBUTO_EXPLICACION = "entrenamientoExplicacion";
  private static final String REDIRECCION_LOGIN = "redirect:/login";
  private static final String REDIRECCION_ENTRENAMIENTO = "redirect:/entrenamiento";
  private static final String REDIRECCION_PREGUNTA = "redirect:/entrenamiento/pregunta";

  private final ServicioEntrenamiento servicioEntrenamiento;
  private final ServicioPregunta servicioPregunta;
  private final ServicioGemini servicioGemini;
  private final ServicioSugerenciaPregunta servicioSugerenciaPregunta;
  private final ServicioProvincia servicioProvincia;

  public ControladorEntrenamiento(
    ServicioEntrenamiento servicioEntrenamiento,
    ServicioPregunta servicioPregunta,
    ServicioGemini servicioGemini,
    ServicioSugerenciaPregunta servicioSugerenciaPregunta,
    ServicioProvincia servicioProvincia
  ) {
    this.servicioEntrenamiento = servicioEntrenamiento;
    this.servicioPregunta = servicioPregunta;
    this.servicioGemini = servicioGemini;
    this.servicioSugerenciaPregunta = servicioSugerenciaPregunta;
    this.servicioProvincia = servicioProvincia;
  }

  @GetMapping
  public ModelAndView inicio(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    ModelMap modelo = new ModelMap();
    modelo.put("usuario", usuario);
    modelo.put("provincias", servicioEntrenamiento.obtenerProvinciasConPreguntas());
    modelo.put("mensaje", session.getAttribute(ATRIBUTO_MENSAJE));
    modelo.put("preguntaGenerada", session.getAttribute(ATRIBUTO_PREGUNTA_GENERADA));
    session.removeAttribute(ATRIBUTO_MENSAJE);
    return new ModelAndView("entrenamiento", modelo);
  }

  @PostMapping("/iniciar")
  public ModelAndView iniciarEntrenamiento(
    @RequestParam(value = "idProvincia", required = false) Long idProvincia,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    limpiarSesionEntrenamiento(session);

    Pregunta pregunta = servicioEntrenamiento.obtenerPreguntaParaEntrenamiento(
      idProvincia,
      new HashSet<>()
    );
    if (pregunta == null) {
      session.setAttribute(ATRIBUTO_MENSAJE, "No hay preguntas cargadas para esa opción.");
      return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
    }

    session.setAttribute(ATRIBUTO_PREGUNTA_ACTUAL, pregunta);
    session.setAttribute(ATRIBUTO_PUNTAJE, 0);
    session.setAttribute(ATRIBUTO_RESPUESTAS, 0);
    session.setAttribute(ATRIBUTO_USADAS, new HashSet<Long>());
    session.setAttribute(ATRIBUTO_FINALIZADO, false);
    session.setAttribute(ATRIBUTO_PROVINCIA, idProvincia);

    return new ModelAndView(REDIRECCION_PREGUNTA);
  }

  @GetMapping("/pregunta")
  public ModelAndView mostrarPregunta(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    Pregunta preguntaActual = (Pregunta) session.getAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
    if (preguntaActual == null) {
      if (Boolean.TRUE.equals(session.getAttribute(ATRIBUTO_FINALIZADO))) {
        return mostrarSalidaEntrenamiento(session, usuario);
      }
      return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
    }

    ModelMap modelo = new ModelMap();
    modelo.put("usuario", usuario);
    modelo.put("pregunta", preguntaActual);
    modelo.put("opciones", servicioPregunta.obtenerOpcionesMezcladas(preguntaActual));
    modelo.put("puntaje", session.getAttribute(ATRIBUTO_PUNTAJE));
    modelo.put("respuestas", session.getAttribute(ATRIBUTO_RESPUESTAS));
    modelo.put("finalizado", session.getAttribute(ATRIBUTO_FINALIZADO));
    modelo.put("mensaje", session.getAttribute(ATRIBUTO_MENSAJE));
    modelo.put("explicacion", session.getAttribute(ATRIBUTO_EXPLICACION));
    session.removeAttribute(ATRIBUTO_MENSAJE);
    session.removeAttribute(ATRIBUTO_EXPLICACION);
    return new ModelAndView("entrenamiento-pregunta", modelo);
  }

  @PostMapping("/salir")
  public ModelAndView salirEntrenamiento(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    int puntaje = obtenerEnteroDesdeSesion(session, ATRIBUTO_PUNTAJE);
    session.setAttribute(ATRIBUTO_FINALIZADO, true);
    session.setAttribute(
      ATRIBUTO_MENSAJE,
      "Saliste del entrenamiento. Tu puntaje fue " + puntaje + "."
    );
    session.removeAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
    session.removeAttribute(ATRIBUTO_EXPLICACION);

    return new ModelAndView(REDIRECCION_PREGUNTA);
  }

  @PostMapping("/generar-pregunta-ia")
  public ModelAndView generarPreguntaIA(
    @RequestParam(value = "idProvincia", required = false) Long idProvincia,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    try {
      Provincia provincia = obtenerProvinciaParaPreguntaIA(idProvincia);
      String contexto =
        "Genera una pregunta educativa sobre la provincia " +
        provincia.getNombre() +
        ". " +
        construirContextoPreguntasExistentes(provincia.getId());
      String prompt =
        "Devuelve exclusivamente un objeto JSON valido, sin markdown, sin texto extra y sin bloque de codigo. " +
        "El formato debe ser: {\"enunciado\":\"...\",\"respuestaCorrecta\":\"...\",\"opcionIncorrectaUno\":\"...\",\"opcionIncorrectaDos\":\"...\",\"opcionIncorrectaTres\":\"...\"}. " +
        contexto +
        " La pregunta debe ser clara, breve, útil para un juego educativo y distinta de las existentes.";
      String respuesta = servicioGemini.preguntar(prompt, null, false);
      PreguntaGeneradaIA generada = parsearPreguntaIA(respuesta, provincia.getId());
      session.setAttribute(ATRIBUTO_PREGUNTA_GENERADA, generada);
      session.setAttribute(
        ATRIBUTO_MENSAJE,
        "Pregunta generada por IA. Revisá y luego podés enviarla como sugerencia."
      );
    } catch (JsonProcessingException | RuntimeException exception) {
      session.setAttribute(
        ATRIBUTO_MENSAJE,
        "No se pudo generar la pregunta con IA: " + exception.getMessage()
      );
    }

    return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
  }

  @PostMapping("/sugerir-pregunta-ia")
  public ModelAndView sugerirPreguntaIA(HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    PreguntaGeneradaIA generada = (PreguntaGeneradaIA) session.getAttribute(
      ATRIBUTO_PREGUNTA_GENERADA
    );
    if (generada == null) {
      session.setAttribute(ATRIBUTO_MENSAJE, "Primero generá una pregunta con IA.");
      return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
    }

    try {
      com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta datos =
        new com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta();
      datos.setEnunciado(generada.getEnunciado());
      datos.setRespuestaCorrecta(generada.getRespuestaCorrecta());
      datos.setOpcionIncorrectaUno(generada.getOpcionIncorrectaUno());
      datos.setOpcionIncorrectaDos(generada.getOpcionIncorrectaDos());
      datos.setOpcionIncorrectaTres(generada.getOpcionIncorrectaTres());
      datos.setIdProvincia(generada.getIdProvincia());
      servicioSugerenciaPregunta.crearSugerencia(datos, usuario);
      session.removeAttribute(ATRIBUTO_PREGUNTA_GENERADA);
      session.setAttribute(
        ATRIBUTO_MENSAJE,
        "¡La pregunta fue enviada como sugerencia al administrador!"
      );
    } catch (RuntimeException exception) {
      session.setAttribute(ATRIBUTO_MENSAJE, exception.getMessage());
    }

    return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
  }

  @PostMapping("/responder")
  public ModelAndView responder(
    @RequestParam("respuesta") String respuesta,
    @RequestParam("idPregunta") Long idPregunta,
    HttpSession session
  ) {
    Usuario usuario = (Usuario) session.getAttribute(ATRIBUTO_USUARIO);
    if (usuario == null) {
      return new ModelAndView(REDIRECCION_LOGIN);
    }

    Pregunta preguntaActual = (Pregunta) session.getAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
    if (preguntaActual == null || !idPregunta.equals(preguntaActual.getId())) {
      return new ModelAndView(REDIRECCION_ENTRENAMIENTO);
    }

    boolean acerto = servicioPregunta.validarRespuesta(idPregunta, respuesta);
    Set<Long> usadas = (Set<Long>) session.getAttribute(ATRIBUTO_USADAS);
    if (usadas == null) {
      usadas = new HashSet<>();
    }

    usadas.add(preguntaActual.getId());
    int puntaje = obtenerEnteroDesdeSesion(session, ATRIBUTO_PUNTAJE) + (acerto ? 1 : 0);
    int respuestas = obtenerEnteroDesdeSesion(session, ATRIBUTO_RESPUESTAS) + 1;
    session.setAttribute(ATRIBUTO_PUNTAJE, puntaje);
    session.setAttribute(ATRIBUTO_RESPUESTAS, respuestas);
    session.setAttribute(ATRIBUTO_USADAS, usadas);

    if (acerto) {
      Pregunta siguiente = servicioEntrenamiento.obtenerPreguntaParaEntrenamiento(
        (Long) session.getAttribute(ATRIBUTO_PROVINCIA),
        usadas
      );

      if (siguiente == null) {
        session.setAttribute(ATRIBUTO_FINALIZADO, true);
        session.setAttribute(
          ATRIBUTO_MENSAJE,
          "¡Entrenamiento finalizado! Llegaste a " + puntaje + " respuestas correctas."
        );
        session.removeAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
      } else {
        session.setAttribute(ATRIBUTO_PREGUNTA_ACTUAL, siguiente);
        session.setAttribute(ATRIBUTO_MENSAJE, "✓ Respuesta correcta. Seguimos.");
      }
    } else {
      generarExplicacionRespuestaIncorrecta(preguntaActual, respuesta, session);
      session.setAttribute(ATRIBUTO_FINALIZADO, true);
      session.setAttribute(
        ATRIBUTO_MENSAJE,
        "❌ Respuesta incorrecta. Fin del entrenamiento. Tu puntaje fue " + puntaje + "."
      );
      session.removeAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
    }

    return new ModelAndView(REDIRECCION_PREGUNTA);
  }

  private int obtenerEnteroDesdeSesion(HttpSession session, String atributo) {
    Object valor = session.getAttribute(atributo);
    if (valor instanceof Integer) {
      return (Integer) valor;
    }
    return 0;
  }

  private ModelAndView mostrarSalidaEntrenamiento(HttpSession session, Usuario usuario) {
    ModelMap modelo = new ModelMap();
    modelo.put("usuario", usuario);
    modelo.put("puntaje", obtenerEnteroDesdeSesion(session, ATRIBUTO_PUNTAJE));
    modelo.put("respuestas", obtenerEnteroDesdeSesion(session, ATRIBUTO_RESPUESTAS));
    modelo.put("finalizado", true);
    modelo.put("mensaje", session.getAttribute(ATRIBUTO_MENSAJE));
    modelo.put("explicacion", session.getAttribute(ATRIBUTO_EXPLICACION));
    session.removeAttribute(ATRIBUTO_MENSAJE);
    session.removeAttribute(ATRIBUTO_EXPLICACION);
    return new ModelAndView("entrenamiento-pregunta", modelo);
  }

  private PreguntaGeneradaIA parsearPreguntaIA(String respuesta, Long idProvincia) {
    try {
      com.fasterxml.jackson.databind.ObjectMapper mapper =
        new com.fasterxml.jackson.databind.ObjectMapper();
      com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(
        extraerJsonPregunta(respuesta)
      );
      PreguntaGeneradaIA generada = new PreguntaGeneradaIA();
      generada.setEnunciado(obtenerCampoObligatorio(root, "enunciado"));
      generada.setRespuestaCorrecta(obtenerCampoObligatorio(root, "respuestaCorrecta"));
      generada.setOpcionIncorrectaUno(obtenerCampoObligatorio(root, "opcionIncorrectaUno"));
      generada.setOpcionIncorrectaDos(obtenerCampoObligatorio(root, "opcionIncorrectaDos"));
      generada.setOpcionIncorrectaTres(obtenerCampoObligatorio(root, "opcionIncorrectaTres"));
      generada.setIdProvincia(idProvincia);
      if (idProvincia != null) {
        generada.setProvinciaNombre(servicioProvincia.buscarPorId(idProvincia).getNombre());
      }
      return generada;
    } catch (Exception exception) {
      throw new RuntimeException("La respuesta de IA no tenía el formato esperado.", exception);
    }
  }

  private String extraerJsonPregunta(String respuesta) {
    if (respuesta == null || respuesta.isBlank()) {
      throw new IllegalArgumentException("La IA no devolvió contenido.");
    }

    String respuestaNormalizada = respuesta
      .trim()
      .replaceFirst("(?s)^```(?:json)?\\s*", "")
      .replaceFirst("(?s)\\s*```$", "")
      .trim();
    int inicioJson = respuestaNormalizada.indexOf('{');
    int finJson = respuestaNormalizada.lastIndexOf('}');

    if (inicioJson < 0 || finJson <= inicioJson) {
      throw new IllegalArgumentException("No se encontró un objeto JSON.");
    }

    return respuestaNormalizada.substring(inicioJson, finJson + 1);
  }

  private String obtenerCampoObligatorio(
    com.fasterxml.jackson.databind.JsonNode root,
    String nombreCampo
  ) {
    String valor = root.path(nombreCampo).asText("").trim();
    if (valor.isBlank()) {
      throw new IllegalArgumentException("Falta el campo " + nombreCampo + ".");
    }
    return valor;
  }

  private Provincia obtenerProvinciaParaPreguntaIA(Long idProvincia) {
    if (idProvincia != null) {
      Provincia provincia = servicioProvincia.buscarPorId(idProvincia);
      if (provincia == null) {
        throw new IllegalArgumentException("La provincia seleccionada no existe.");
      }
      return provincia;
    }

    List<Provincia> provincias = new ArrayList<>(
      servicioEntrenamiento.obtenerProvinciasConPreguntas()
    );
    if (provincias.isEmpty()) {
      provincias = new ArrayList<>(servicioProvincia.obtenerProvincias());
    }
    if (provincias.isEmpty()) {
      throw new IllegalStateException("No hay provincias disponibles para generar preguntas.");
    }

    Collections.shuffle(provincias);
    return provincias.get(0);
  }

  private String construirContextoPreguntasExistentes(Long idProvincia) {
    List<Pregunta> preguntasExistentes = servicioPregunta.buscarPorProvincia(idProvincia);
    if (preguntasExistentes == null || preguntasExistentes.isEmpty()) {
      return "Todavía no hay preguntas cargadas para esa provincia.";
    }

    StringBuilder contexto = new StringBuilder(
      "Ya existen estas preguntas para esa provincia. No generes una pregunta igual, muy parecida, ni con la misma respuesta correcta: "
    );

    preguntasExistentes
      .stream()
      .limit(15)
      .forEach(pregunta ->
        contexto
          .append("Pregunta: '")
          .append(pregunta.getEnunciado())
          .append("'. Respuesta correcta: '")
          .append(pregunta.getRespuestaCorrecta())
          .append("'. ")
      );

    return contexto.toString();
  }

  private void generarExplicacionRespuestaIncorrecta(
    Pregunta pregunta,
    String respuestaUsuario,
    HttpSession session
  ) {
    try {
      String prompt =
        "El usuario eligió la respuesta incorrecta '" +
        respuestaUsuario +
        "' para esta pregunta: '" +
        pregunta.getEnunciado() +
        "'. Explica brevemente por qué esa opción es incorrecta. " +
        "No centres la explicación en justificar la respuesta correcta; mencionarla solo al final como dato de cierre: '" +
        pregunta.getRespuestaCorrecta() +
        "'. Sé conciso, máximo 2-3 oraciones educativas.";
      String explicacion = servicioGemini.preguntar(prompt, null, false);
      session.setAttribute(ATRIBUTO_EXPLICACION, "📚 " + explicacion);
    } catch (Exception e) {
      session.setAttribute(ATRIBUTO_EXPLICACION, "❌ Respuesta incorrecta");
    }
  }

  private void limpiarSesionEntrenamiento(HttpSession session) {
    session.removeAttribute(ATRIBUTO_PREGUNTA_ACTUAL);
    session.removeAttribute(ATRIBUTO_PUNTAJE);
    session.removeAttribute(ATRIBUTO_RESPUESTAS);
    session.removeAttribute(ATRIBUTO_USADAS);
    session.removeAttribute(ATRIBUTO_FINALIZADO);
    session.removeAttribute(ATRIBUTO_MENSAJE);
    session.removeAttribute(ATRIBUTO_PROVINCIA);
    session.removeAttribute(ATRIBUTO_PREGUNTA_GENERADA);
    session.removeAttribute(ATRIBUTO_EXPLICACION);
  }
}
