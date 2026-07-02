package com.tallerwebi.controladores;

import com.tallerwebi.servicios.ServicioPago;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
public class ControladorWebhookMP {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControladorWebhookMP.class);

  private static final String EVENT_TYPE_KEY = "type";
  private static final String PAYMENT_EVENT_VALUE = "payment";
  private static final String DATA_KEY = "data";
  private static final String ID_KEY = "id";
  private static final String RESPONSE_OK_MESSAGE = "Recibido";

  private final ServicioPago servicioPago;

  @Autowired
  public ControladorWebhookMP(ServicioPago servicioPago) {
    this.servicioPago = servicioPago;
  }

  @PostMapping("/webhook")
  public ResponseEntity<String> recibirNotificacion(@RequestBody Map<String, Object> payload) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Payload recibido: {}", payload);
    }

    try {
      if (
        payload.containsKey(EVENT_TYPE_KEY) &&
        PAYMENT_EVENT_VALUE.equals(payload.get(EVENT_TYPE_KEY))
      ) {
        Object dataObject = payload.get(DATA_KEY);

        if (dataObject instanceof Map) {
          @SuppressWarnings("unchecked")
          Map<String, Object> data = (Map<String, Object>) dataObject;

          if (data.containsKey(ID_KEY) && data.get(ID_KEY) != null) {
            Long idPago = Long.valueOf(data.get(ID_KEY).toString());
            servicioPago.procesarPagoAprobado(idPago);
          }
        }
      }
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Error procesando payload de MP: {}", e.getMessage(), e);
      }
    }

    return ResponseEntity.ok(RESPONSE_OK_MESSAGE);
  }
}
