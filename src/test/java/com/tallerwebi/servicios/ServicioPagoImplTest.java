package com.tallerwebi.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.entidades.PagoProcesado;
import com.tallerwebi.entidades.PaqueteMonedas;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPago;
import com.tallerwebi.servicios.Impl.ServicioPagoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicioPagoImplTest {

  @Mock
  private ServicioTienda servicioTienda;

  @Mock
  private ServicioUsuario servicioUsuario;

  @Mock
  private RepositorioPago repositorioPago;

  @InjectMocks
  private ServicioPagoImpl servicioPago;

  @Test
  public void alCrearPreferenciaDePagoExitosamenteDebeRetornarInitPoint() throws Exception {
    PaqueteMonedas paquete = new PaqueteMonedas();
    paquete.setId(10L);
    paquete.setTitulo("Paquete Test");
    paquete.setCantidadCoins(100);
    paquete.setPrecioArs(500.0);

    Long idUsuario = 1L;
    String initPointEsperado = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=999";

    try (
      MockedConstruction<PreferenceClient> mocked = mockConstruction(
        PreferenceClient.class,
        (mock, context) -> {
          Preference preference = mock(Preference.class);
          when(preference.getInitPoint()).thenReturn(initPointEsperado);
          when(mock.create(any(PreferenceRequest.class))).thenReturn(preference);
        }
      )
    ) {
      String result = servicioPago.crearPreferenciaDePago(paquete, idUsuario);

      assertEquals(initPointEsperado, result);
    }
  }

  @Test
  public void alCrearPreferenciaConErrorDeMPApiDebeLanzarRuntimeException() throws Exception {
    PaqueteMonedas paquete = new PaqueteMonedas();
    paquete.setId(10L);
    paquete.setTitulo("Paquete Test");
    paquete.setCantidadCoins(100);
    paquete.setPrecioArs(500.0);

    Long idUsuario = 1L;

    MPResponse apiResponse = mock(MPResponse.class);
    lenient().when(apiResponse.getContent()).thenReturn("{\"message\":\"invalid_token\"}");

    MPApiException apiException = mock(MPApiException.class);
    lenient().when(apiException.getApiResponse()).thenReturn(apiResponse);

    try (
      MockedConstruction<PreferenceClient> mocked = mockConstruction(
        PreferenceClient.class,
        (mock, context) -> {
          when(mock.create(any(PreferenceRequest.class))).thenThrow(apiException);
        }
      )
    ) {
      assertThrows(
        RuntimeException.class,
        () -> {
          servicioPago.crearPreferenciaDePago(paquete, idUsuario);
        }
      );
    }
  }

  @Test
  public void alProcesarPagoAprobadoQueYaExisteNoDebeHacerNada() {
    Long idPago = 12345L;
    PagoProcesado pagoExistente = new PagoProcesado(idPago);

    when(repositorioPago.buscarPagoPorId(idPago)).thenReturn(pagoExistente);

    servicioPago.procesarPagoAprobado(idPago);

    verify(repositorioPago).buscarPagoPorId(idPago);
    verify(repositorioPago, never()).guardarPago(any(PagoProcesado.class));
    verifyNoInteractions(servicioTienda, servicioUsuario);
  }

  @Test
  public void alProcesarPagoAprobadoExitosamenteDebeAcreditarMonedasYGuardarPago()
    throws Exception {
    Long idPago = 12345L;
    String externalReference = "1_10";

    when(repositorioPago.buscarPagoPorId(idPago)).thenReturn(null);

    PaqueteMonedas paquete = new PaqueteMonedas();
    paquete.setId(10L);
    paquete.setCantidadCoins(150);

    Usuario usuario = new Usuario();
    usuario.setId(1L);

    when(servicioTienda.buscarPaquetePorId(10L)).thenReturn(paquete);
    when(servicioUsuario.buscarUsuarioPorId(1L)).thenReturn(usuario);

    try (
      MockedConstruction<PaymentClient> mocked = mockConstruction(
        PaymentClient.class,
        (mock, context) -> {
          Payment payment = mock(Payment.class);
          when(payment.getStatus()).thenReturn("approved");
          when(payment.getExternalReference()).thenReturn(externalReference);
          when(mock.get(idPago)).thenReturn(payment);
        }
      )
    ) {
      servicioPago.procesarPagoAprobado(idPago);

      verify(servicioUsuario).actualizarUsuario(usuario);
      verify(repositorioPago).guardarPago(any(PagoProcesado.class));
    }
  }

  @Test
  public void alProcesarPagoNoAprobadoNoDebeAcreditarNiGuardarPago() throws Exception {
    Long idPago = 12345L;

    when(repositorioPago.buscarPagoPorId(idPago)).thenReturn(null);

    try (
      MockedConstruction<PaymentClient> mocked = mockConstruction(
        PaymentClient.class,
        (mock, context) -> {
          Payment payment = mock(Payment.class);
          when(payment.getStatus()).thenReturn("pending");
          when(mock.get(idPago)).thenReturn(payment);
        }
      )
    ) {
      servicioPago.procesarPagoAprobado(idPago);

      verify(repositorioPago, never()).guardarPago(any(PagoProcesado.class));
      verifyNoInteractions(servicioTienda, servicioUsuario);
    }
  }

  @Test
  public void alProcesarPagoConReferenciaInvalidaNoDebeAcreditarMonedas() throws Exception {
    Long idPago = 12345L;
    String externalReferenceInvalida = "referenciaMalFormada";

    when(repositorioPago.buscarPagoPorId(idPago)).thenReturn(null);

    try (
      MockedConstruction<PaymentClient> mocked = mockConstruction(
        PaymentClient.class,
        (mock, context) -> {
          Payment payment = mock(Payment.class);
          when(payment.getStatus()).thenReturn("approved");
          when(payment.getExternalReference()).thenReturn(externalReferenceInvalida);
          when(mock.get(idPago)).thenReturn(payment);
        }
      )
    ) {
      servicioPago.procesarPagoAprobado(idPago);

      verify(repositorioPago, never()).guardarPago(any(PagoProcesado.class));
      verifyNoInteractions(servicioTienda, servicioUsuario);
    }
  }
}
