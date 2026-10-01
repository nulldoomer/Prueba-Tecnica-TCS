package com.prueba.tcs.clientes.cliente.event;

import com.prueba.tcs.clientes.cliente.entity.ClienteEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published to RabbitMQ when a cliente changes.
 *
 * @param eventId    unique id of this event
 * @param eventType  what happened to the cliente
 * @param clienteId  id of the affected cliente
 * @param nombre     current nombre of the cliente
 * @param estado     current estado of the cliente
 * @param occurredAt when the change happened, used by consumers
 */
public record ClienteEvent(
        UUID eventId,
        Type eventType,
        UUID clienteId,
        String nombre,
        Boolean estado,
        LocalDateTime occurredAt
) {

    public enum Type {
        CREATED,
        UPDATED,
        DEACTIVATED
    }

    public static ClienteEvent of(Type type, ClienteEntity cliente) {

        return new ClienteEvent(UUID.randomUUID(), type, cliente.getId(),
                cliente.getNombre(), cliente.getEstado(),
                LocalDateTime.now()
        );
    }

    /**
     * Routing key used in the topic exchange, e.g. {@code cliente.created}.
     */
    public String routingKey() {
        return "cliente." + eventType.name().toLowerCase();
    }
}
