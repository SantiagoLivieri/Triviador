package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.entidades.Comodin;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioComodin;
import com.tallerwebi.repositorios.RepositorioUsuario;
import com.tallerwebi.servicios.Impl.ServicioTiendaImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioTiendaImplTest {

  /*
   * Mockito:
   *
   * Mockeamos RepositorioComodin porque no queremos consultar comodines reales
   * en la base de datos.
   */
  @Mock
  private RepositorioComodin repositorioComodin;

  /*
   * Mockito:
   *
   * Mockeamos RepositorioUsuario porque no queremos buscar ni actualizar usuarios reales.
   */
  @Mock
  private RepositorioUsuario repositorioUsuario;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioTiendaImpl
   * e inyecta los repositorios mockeados en el constructor.
   */
  @InjectMocks
  private ServicioTiendaImpl servicioTienda;

  @Test
  public void alProcesarCompraDeComodinDebeBuscarUsuarioBuscarComodinAdquirirloYActualizarUsuario() {
    /*
     * Caso feliz:
     *
     * El usuario existe.
     * El comodín existe.
     *
     * Entonces:
     * 1. el usuario adquiere el comodín
     * 2. el usuario se actualiza en el repositorio
     */

    // Preparación
    Long idUsuario = 1L;
    String nombreComodin = "DobleChance";

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);
    Comodin comodin = org.mockito.Mockito.mock(Comodin.class);

    when(repositorioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
    when(repositorioComodin.buscarPorNombre(nombreComodin)).thenReturn(comodin);

    // Ejecución
    servicioTienda.procesarCompraDeComodin(idUsuario, nombreComodin);

    // Verificación
    verify(repositorioUsuario).buscarUsuarioPorId(idUsuario);
    verify(repositorioComodin).buscarPorNombre(nombreComodin);

    verify(usuario).adquirirComodin(comodin);
    verify(repositorioUsuario).actualizarUsuario(usuario);
  }

  @Test
  public void alProcesarCompraConUsuarioInexistenteDebeLanzarIllegalArgumentException() {
    /*
     * Cubre esta rama:
     *
     * if (usuario == null)
     *
     * Si no existe el usuario, no debe buscar el comodín
     * ni actualizar nada.
     */

    // Preparación
    Long idUsuario = 1L;
    String nombreComodin = "DobleChance";

    when(repositorioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioTienda.procesarCompraDeComodin(idUsuario, nombreComodin)
    );

    verify(repositorioUsuario).buscarUsuarioPorId(idUsuario);
    verify(repositorioComodin, never()).buscarPorNombre(nombreComodin);
  }

  @Test
  public void alProcesarCompraConComodinInexistenteDebeLanzarIllegalArgumentException() {
    /*
     * Cubre esta rama:
     *
     * if (comodin == null)
     *
     * Si el comodín no existe, no debe actualizar al usuario.
     */

    // Preparación
    Long idUsuario = 1L;
    String nombreComodin = "ComodinInexistente";

    Usuario usuario = org.mockito.Mockito.mock(Usuario.class);

    when(repositorioUsuario.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
    when(repositorioComodin.buscarPorNombre(nombreComodin)).thenReturn(null);

    // Ejecución + Verificación
    assertThrows(
      IllegalArgumentException.class,
      () -> servicioTienda.procesarCompraDeComodin(idUsuario, nombreComodin)
    );

    verify(repositorioUsuario).buscarUsuarioPorId(idUsuario);
    verify(repositorioComodin).buscarPorNombre(nombreComodin);

    verify(usuario, never()).adquirirComodin(org.mockito.Mockito.any(Comodin.class));
    verify(repositorioUsuario, never()).actualizarUsuario(usuario);
  }

  @Test
  public void alObtenerCatalogoDeComodinesDebeRetornarTodosLosComodinesDelRepositorio() {
    /*
     * Cubre obtenerCatalogoDeComodines().
     *
     * El servicio simplemente delega en repositorioComodin.buscarTodos().
     */

    // Preparación
    Comodin comodinUno = org.mockito.Mockito.mock(Comodin.class);
    Comodin comodinDos = org.mockito.Mockito.mock(Comodin.class);

    List<Comodin> catalogoEsperado = List.of(comodinUno, comodinDos);

    when(repositorioComodin.buscarTodos()).thenReturn(catalogoEsperado);

    // Ejecución
    List<Comodin> catalogoObtenido = servicioTienda.obtenerCatalogoDeComodines();

    // Verificación
    assertSame(catalogoEsperado, catalogoObtenido);
    verify(repositorioComodin).buscarTodos();
  }
}
