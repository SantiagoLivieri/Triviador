package com.tallerwebi.servicios;

import static org.mockito.Mockito.verify;

import com.tallerwebi.servicios.Impl.ServicioCargaInicialImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioCargaInicialImplTest {

  /*
   * Mockito:
   *
   * Mockeamos ServicioUsuario porque no queremos ejecutar una carga real
   * de roles ni usuario admin.
   *
   * Solo queremos comprobar que ServicioCargaInicialImpl llame
   * a los métodos correctos.
   */
  @Mock
  private ServicioUsuario servicioUsuario;

  /*
   * Mockito:
   *
   * @InjectMocks crea una instancia real de ServicioCargaInicialImpl
   * e inyecta el mock servicioUsuario en el constructor.
   */
  @InjectMocks
  private ServicioCargaInicialImpl servicioCargaInicial;

  @Test
  public void alCargarDatosInicialesDebeCargarRolesYUsuarioAdmin() {
    /*
     * Este test cubre cargarDatosIniciales().
     *
     * El método debe:
     * 1. cargar los roles iniciales
     * 2. cargar el usuario admin inicial
     */

    // Ejecución
    servicioCargaInicial.cargarDatosIniciales();

    // Verificación
    verify(servicioUsuario).cargarRolesIniciales();
    verify(servicioUsuario).cargarUsuarioAdminInicial();
  }
}
