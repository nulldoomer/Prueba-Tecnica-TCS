package com.prueba.tcs.clientes.cliente.event;

import com.prueba.tcs.clientes.infrastructure.config.RabbitConfig;
import com.prueba.tcs.clientes.infrastructure.correlation.CorrelationIdFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Sends {@link ClienteEvent}s to RabbitMQ once the transaction that produced them has committed,
 * so a rolled back change never reaches other services.
 * <p>
 * Known limitation: if RabbitMQ is down the change is already committed and the event is lost (only logged).
 * The Outbox pattern would close that gap.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(ClienteEvent event) {

        String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, event.routingKey(),
                    event, message ->
                    {
                        message.getMessageProperties().setCorrelationId(correlationId);
                        return message;
                    }
            );

            log.info("Published {} for cliente {}", event.routingKey(), event.clienteId());

        } catch (AmqpException ex) {

            log.error("Could not publish {} for cliente {}", event.routingKey(),
                    event.clienteId(), ex
            );
        }
    }
}
