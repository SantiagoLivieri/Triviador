package com.tallerwebi.servicios.Impl;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.entidades.PagoProcesado;
import com.tallerwebi.entidades.PaqueteMonedas;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPago;
import com.tallerwebi.servicios.ServicioPago;
import com.tallerwebi.servicios.ServicioTienda;
import com.tallerwebi.servicios.ServicioUsuario;
import java.math.BigDecimal;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServicioPagoImpl implements ServicioPago {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServicioPagoImpl.class);

  // esta url puede cambiar cada vez q se levanta el proyecto
  private static final String BASE_URL_NGROK = "https://slot-fascism-repossess.ngrok-free.dev";

  private static final String ESTADO_APROBADO = "approved";
  private static final String SEPARADOR_REFERENCIA = "_";
  private static final int CANTIDAD_DATOS_REFERENCIA = 2;

  private final ServicioTienda servicioTienda;
  private final ServicioUsuario servicioUsuario;
  private final RepositorioPago repositorioPago;

  @Value("${mercadopago.access-token}")
  private String accessToken;

  public ServicioPagoImpl(
    ServicioTienda servicioTienda,
    ServicioUsuario servicioUsuario,
    RepositorioPago repositorioPago
  ) {
    this.servicioTienda = servicioTienda;
    this.servicioUsuario = servicioUsuario;
    this.repositorioPago = repositorioPago;
  }

  @PostConstruct
  public void inicializarMercadoPago() {
    MercadoPagoConfig.setAccessToken(accessToken);
  }

  @Override
  public String crearPreferenciaDePago(PaqueteMonedas paquete, Long idUsuario) {
    try {
      String referenciaExterna = idUsuario + SEPARADOR_REFERENCIA + paquete.getId();

      PreferenceRequest preferenceRequest = PreferenceRequest
        .builder()
        .items(Collections.singletonList(crearItemPreferencia(paquete)))
        .backUrls(crearBackUrls())
        .autoReturn("approved")
        .externalReference(referenciaExterna)
        .notificationUrl(BASE_URL_NGROK + "/spring/api/pagos/webhook")
        .build();

      PreferenceClient client = new PreferenceClient();
      Preference preference = client.create(preferenceRequest);

      return preference.getInitPoint();
    } catch (com.mercadopago.exceptions.MPApiException apiException) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Rechazo de MP: {}", apiException.getApiResponse().getContent(), apiException);
      }
      throw new RuntimeException("Error en API MercadoPago", apiException);
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Error general armando pago: {}", e.getMessage(), e);
      }
      throw new RuntimeException("Error general armando pago", e);
    }
  }

  @Override
  @Transactional
  public void procesarPagoAprobado(Long idPago) {
    try {
      PagoProcesado pagoExistente = repositorioPago.buscarPagoPorId(idPago);
      if (pagoExistente != null) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn(
            "Ataque de repetición o evento duplicado evitado. El pago ID {} ya fue procesado.",
            idPago
          );
        }
        return;
      }
      PaymentClient client = new PaymentClient();
      Payment pago = client.get(idPago);

      if (
        pago != null &&
        ESTADO_APROBADO.equals(pago.getStatus()) &&
        pago.getExternalReference() != null
      ) {
        boolean monedasAcreditadas = acreditarMonedasSiCorresponde(pago.getExternalReference());

        if (monedasAcreditadas) {
          PagoProcesado nuevoPagoProcesado = new PagoProcesado(idPago);
          repositorioPago.guardarPago(nuevoPagoProcesado);
        }
      }
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Error procesando payload de MP: {}", e.getMessage(), e);
      }
    }
  }

  private boolean acreditarMonedasSiCorresponde(String externalReference) {
    String[] datos = externalReference.split(SEPARADOR_REFERENCIA);
    if (datos.length == CANTIDAD_DATOS_REFERENCIA) {
      Long idUsuario = Long.valueOf(datos[0]);
      Long idPaquete = Long.valueOf(datos[1]);

      PaqueteMonedas paquete = servicioTienda.buscarPaquetePorId(idPaquete);
      Usuario usuario = servicioUsuario.buscarUsuarioPorId(idUsuario);

      if (usuario != null && paquete != null) {
        usuario.sumarMonedas(paquete.getCantidadCoins());
        servicioUsuario.actualizarUsuario(usuario);

        if (LOGGER.isInfoEnabled()) {
          LOGGER.info(
            "Pago exitoso. Se entregaron {} Coins al usuario {}",
            paquete.getCantidadCoins(),
            usuario.getNombreJugador()
          );
        }
        return true;
      }
    }
    return false;
  }

  private PreferenceItemRequest crearItemPreferencia(PaqueteMonedas paquete) {
    return PreferenceItemRequest
      .builder()
      .id(paquete.getId().toString())
      .title(paquete.getTitulo())
      .description("Recarga de " + paquete.getCantidadCoins() + " TriviaCoins")
      .quantity(1)
      .currencyId("ARS")
      .unitPrice(new BigDecimal(paquete.getPrecioArs().toString()))
      .build();
  }

  private PreferenceBackUrlsRequest crearBackUrls() {
    String urlBase = BASE_URL_NGROK + "/spring";
    return PreferenceBackUrlsRequest
      .builder()
      .success(urlBase + "/tienda/exito")
      .pending(urlBase + "/tienda/pendiente")
      .failure(urlBase + "/tienda/error")
      .build();
  }
}
