package com.tallerwebi.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.tallerwebi.servicios.ServicioPago;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ControladorWebhookMPTest {

  @Mock
  private ServicioPago servicioPago;

  @InjectMocks
  private ControladorWebhookMP controladorWebhookMP;

  @Test
  public void alRecibirPayloadDePagoValidoDebeProcesarPagoYRetornarOk() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "payment");
    Map<String, Object> data = new HashMap<>();
    data.put("id", "123456789");
    payload.put("data", data);

    ResponseEntity<String> response = controladorWebhookMP.recibirNotificacion(payload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Recibido", response.getBody());
    verify(servicioPago).procesarPagoAprobado(123456789L);
  }

  @Test
  public void alRecibirPayloadConOtroTipoDeEventoNoDebeProcesarPagoYRetornarOk() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "merchant_order");

    ResponseEntity<String> response = controladorWebhookMP.recibirNotificacion(payload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Recibido", response.getBody());
    verifyNoInteractions(servicioPago);
  }

  @Test
  public void alRecibirPayloadSinDataNoDebeProcesarPagoYRetornarOk() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "payment");

    ResponseEntity<String> response = controladorWebhookMP.recibirNotificacion(payload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Recibido", response.getBody());
    verifyNoInteractions(servicioPago);
  }

  @Test
  public void alLanzarExcepcionDuranteElProcesamientoDebeCapturarlaYRetornarOk() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "payment");
    Map<String, Object> data = new HashMap<>();
    data.put("id", "999999");
    payload.put("data", data);

    doThrow(new RuntimeException("Error simulado"))
      .when(servicioPago)
      .procesarPagoAprobado(999999L);

    ResponseEntity<String> response = controladorWebhookMP.recibirNotificacion(payload);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Recibido", response.getBody());
    verify(servicioPago).procesarPagoAprobado(999999L);
  }
}
