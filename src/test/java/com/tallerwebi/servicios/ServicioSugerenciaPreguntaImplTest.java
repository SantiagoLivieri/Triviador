package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tallerwebi.controladores.clasesAuxiliares.DatosSugerenciaPregunta;
import com.tallerwebi.entidades.*;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.repositorios.RepositorioSugerenciaPregunta;
import com.tallerwebi.servicios.Impl.ServicioSugerenciaPreguntaImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioSugerenciaPreguntaImplTest {

  private RepositorioSugerenciaPregunta repositorioSugerenciaPregunta;
  private RepositorioPregunta repositorioPregunta;
  private RepositorioProvincia repositorioProvincia;

  private ServicioSugerenciaPreguntaImpl servicio;

  @BeforeEach
  public void init() {
    repositorioSugerenciaPregunta = mock(RepositorioSugerenciaPregunta.class);
    repositorioPregunta = mock(RepositorioPregunta.class);
    repositorioProvincia = mock(RepositorioProvincia.class);

    servicio =
      new ServicioSugerenciaPreguntaImpl(
        repositorioSugerenciaPregunta,
        repositorioPregunta,
        repositorioProvincia
      );
  }

  @Test
  public void dadoUnJugadorValidoCuandoCreaSugerenciaEntoncesSeGuarda() {
    DatosSugerenciaPregunta datos = crearDatos();
    Usuario jugador = crearUsuario("JUGADOR");
    Provincia provincia = new Provincia("Buenos Aires", 10);

    when(repositorioProvincia.buscarPorId(1L)).thenReturn(provincia);

    servicio.crearSugerencia(datos, jugador);

    verify(repositorioSugerenciaPregunta).guardar(any(SugerenciaPregunta.class));
  }

  @Test
  public void dadoUsuarioNoJugadorCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    Usuario admin = crearUsuario("ADMIN");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, admin)
    );

    assertEquals("Solo los usuarios con rol JUGADOR pueden sugerir preguntas.", e.getMessage());
  }

  @Test
  public void dadoAdminValidoCuandoCreaPreguntaEntoncesSeGuarda() {
    DatosSugerenciaPregunta datos = crearDatos();
    Usuario admin = crearUsuario("ADMIN");
    Provincia provincia = new Provincia("Buenos Aires", 10);

    when(repositorioProvincia.buscarPorId(1L)).thenReturn(provincia);

    servicio.crearPreguntaComoAdmin(datos, admin);

    verify(repositorioPregunta).guardar(any(Pregunta.class));
  }

  @Test
  public void dadoUsuarioNoAdminCuandoCreaPreguntaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    Usuario jugador = crearUsuario("JUGADOR");

    assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearPreguntaComoAdmin(datos, jugador)
    );
  }

  @Test
  public void obtenerSugerenciasPendientesRetornaListaDelRepositorio() {
    List<SugerenciaPregunta> sugerencias = List.of(mock(SugerenciaPregunta.class));

    when(repositorioSugerenciaPregunta.buscarPorEstado(EstadoSugerenciaPregunta.PENDIENTE))
      .thenReturn(sugerencias);

    List<SugerenciaPregunta> resultado = servicio.obtenerSugerenciasPendientes();

    assertEquals(sugerencias, resultado);
  }

  @Test
  public void obtenerTodasRetornaListaDelRepositorio() {
    List<SugerenciaPregunta> sugerencias = List.of(mock(SugerenciaPregunta.class));

    when(repositorioSugerenciaPregunta.buscarTodas()).thenReturn(sugerencias);

    List<SugerenciaPregunta> resultado = servicio.obtenerTodas();

    assertEquals(sugerencias, resultado);
  }

  @Test
  public void buscarPorIdRetornaLaSugerenciaCorrespondiente() {
    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(sugerencia);

    SugerenciaPregunta resultado = servicio.buscarPorId(1L);

    assertEquals(sugerencia, resultado);
  }

  @Test
  public void dadoAdminYUnaSugerenciaExistenteCuandoApruebaEntoncesGuardaPreguntaYActualiza() {
    Usuario admin = crearUsuario("ADMIN");
    Provincia provincia = new Provincia("Buenos Aires", 10);

    SugerenciaPregunta sugerencia = new SugerenciaPregunta(
      "Enunciado",
      "Correcta",
      "Incorrecta1",
      "Incorrecta2",
      "Incorrecta3",
      provincia,
      crearUsuario("JUGADOR")
    );

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(sugerencia);

    servicio.aprobarSugerencia(1L, admin);

    verify(repositorioPregunta).guardar(any(Pregunta.class));
    verify(repositorioSugerenciaPregunta).actualizar(sugerencia);

    assertEquals(EstadoSugerenciaPregunta.APROBADA, sugerencia.getEstado());
  }

  @Test
  public void dadoUnaSugerenciaInexistenteCuandoApruebaEntoncesLanzaExcepcion() {
    Usuario admin = crearUsuario("ADMIN");

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(null);

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.aprobarSugerencia(1L, admin)
    );

    assertEquals("La sugerencia no existe.", e.getMessage());
  }

  @Test
  public void dadoUnaSugerenciaExistenteCuandoActualizaEntoncesActualizaRepositorio() {
    Usuario admin = crearUsuario("ADMIN");
    Provincia provincia = new Provincia("Buenos Aires", 10);

    DatosSugerenciaPregunta datos = crearDatos();
    datos.setId(1L);

    SugerenciaPregunta sugerencia = new SugerenciaPregunta(
      "Pregunta vieja",
      "Correcta vieja",
      "Inc1",
      "Inc2",
      "Inc3",
      provincia,
      crearUsuario("JUGADOR")
    );

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(sugerencia);

    when(repositorioProvincia.buscarPorId(1L)).thenReturn(provincia);

    servicio.actualizarSugerencia(datos, admin);

    assertEquals("Pregunta", sugerencia.getEnunciado());
    assertEquals("Correcta", sugerencia.getRespuestaCorrecta());

    verify(repositorioSugerenciaPregunta).actualizar(sugerencia);
  }

  @Test
  public void dadoDatosSinIdCuandoActualizaEntoncesLanzaExcepcion() {
    Usuario admin = crearUsuario("ADMIN");

    DatosSugerenciaPregunta datos = crearDatos();

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.actualizarSugerencia(datos, admin)
    );

    assertEquals("No se encontró la sugerencia a editar.", e.getMessage());
  }

  @Test
  public void dadoDatoNullCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(null, crearUsuario("JUGADOR"))
    );

    assertEquals("Los datos de la sugerencia son obligatorios.", e.getMessage());
  }

  @Test
  public void dadoIdProvinciaNullCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setIdProvincia(null);

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("Debe seleccionar una provincia.", e.getMessage());
  }

  @Test
  public void dadoRespuestaCorrectaVaciaCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setRespuestaCorrecta(" ");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("La respuesta correcta no puede estar vacía.", e.getMessage());
  }

  @Test
  public void dadoOpcionIncorrectaUnoVaciaCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setOpcionIncorrectaUno(" ");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("La primera respuesta incorrecta no puede estar vacía.", e.getMessage());
  }

  @Test
  public void dadoOpcionIncorrectaDosVaciaCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setOpcionIncorrectaDos(" ");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("La segunda respuesta incorrecta no puede estar vacía.", e.getMessage());
  }

  @Test
  public void dadoOpcionIncorrectaTresVaciaCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setOpcionIncorrectaTres(" ");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("La tercera respuesta incorrecta no puede estar vacía.", e.getMessage());
  }

  @Test
  public void dadoUnaSugerenciaExistenteCuandoEliminaEntoncesSeElimina() {
    Usuario admin = crearUsuario("ADMIN");

    SugerenciaPregunta sugerencia = mock(SugerenciaPregunta.class);

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(sugerencia);

    servicio.eliminarSugerencia(1L, admin);

    verify(repositorioSugerenciaPregunta).eliminar(sugerencia);
  }

  @Test
  public void dadoUnaSugerenciaInexistenteCuandoSeEliminaEntoncesLanzaExcepcion() {
    Usuario admin = crearUsuario("ADMIN");

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(null);

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.eliminarSugerencia(1L, admin)
    );

    assertEquals("La sugerencia no existe.", e.getMessage());
  }

  @Test
  public void dadoUnaSugerenciaInexistenteCuandoSeActualizaEntoncesLanzaExcepcion() {
    Usuario admin = crearUsuario("ADMIN");

    DatosSugerenciaPregunta datos = crearDatos();
    datos.setId(1L);

    when(repositorioSugerenciaPregunta.buscarPorId(1L)).thenReturn(null);

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.actualizarSugerencia(datos, admin)
    );

    assertEquals("La sugerencia no existe.", e.getMessage());
  }

  @Test
  public void dadoProvinciaInexistenteCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();

    when(repositorioProvincia.buscarPorId(1L)).thenReturn(null);

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("La provincia seleccionada no existe.", e.getMessage());
  }

  @Test
  public void dadoEnunciadoVacioCuandoCreaSugerenciaEntoncesLanzaExcepcion() {
    DatosSugerenciaPregunta datos = crearDatos();
    datos.setEnunciado(" ");

    IllegalArgumentException e = assertThrows(
      IllegalArgumentException.class,
      () -> servicio.crearSugerencia(datos, crearUsuario("JUGADOR"))
    );

    assertEquals("El enunciado no puede estar vacío.", e.getMessage());
  }

  private DatosSugerenciaPregunta crearDatos() {
    DatosSugerenciaPregunta datos = new DatosSugerenciaPregunta();

    datos.setIdProvincia(1L);
    datos.setEnunciado("Pregunta");
    datos.setRespuestaCorrecta("Correcta");
    datos.setOpcionIncorrectaUno("Incorrecta1");
    datos.setOpcionIncorrectaDos("Incorrecta2");
    datos.setOpcionIncorrectaTres("Incorrecta3");

    return datos;
  }

  private Usuario crearUsuario(String descripcionRol) {
    Usuario usuario = mock(Usuario.class);
    Rol rol = mock(Rol.class);

    when(usuario.getRol()).thenReturn(rol);
    when(rol.getDescripcion()).thenReturn(descripcionRol);

    return usuario;
  }
}
