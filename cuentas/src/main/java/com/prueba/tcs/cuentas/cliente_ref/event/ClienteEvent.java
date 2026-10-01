package com.prueba.tcs.cuentas.cliente_ref.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event received from the clientes service. It is a copy of the producer's contract:
 * each service keeps its own class so they can be deployed independently.
 *
 * @param eventId    unique id of this event
 * @param eventType  what happened to the cliente
 * @param clienteId  id of the affected cliente
 * @param nombre     current nombre of the cliente
 * @param estado     current estado of the cliente
 * @param occurredAt when the change happened in clientes
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
}
