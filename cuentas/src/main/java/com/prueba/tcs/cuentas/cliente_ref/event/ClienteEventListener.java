package com.prueba.tcs.cuentas.cliente_ref.event;

import com.prueba.tcs.cuentas.cliente_ref.service.ClienteRefService;
import com.prueba.tcs.cuentas.infrastructure.config.RabbitConfig;
import com.prueba.tcs.cuentas.infrastructure.correlation.CorrelationIdFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes cliente events from RabbitMQ and keeps the local cliente_ref copy up to date.
 * The correlation id sent by clientes is put in the MDC so both services share it in their logs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ClienteEventListener {

    private final ClienteRefService clienteRefService;

    @RabbitListener(queues = RabbitConfig.CLIENTE_EVENTS_QUEUE)
    public void onClienteEvent(
            @Payload ClienteEvent event,
            @Header(name = AmqpHeaders.CORRELATION_ID, required = false) String correlationId) {

        if (correlationId != null) {
            MDC.put(CorrelationIdFilter.MDC_KEY, correlationId);
        }
        try {
            log.info("Received {} for cliente {}", event.eventType(), event.clienteId());
            clienteRefService.updateClientRef(event);
        } finally {
            MDC.remove(CorrelationIdFilter.MDC_KEY);
        }
    }
}
