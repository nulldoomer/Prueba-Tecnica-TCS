package com.prueba.tcs.cuentas.cliente_ref.service;

import com.prueba.tcs.cuentas.cliente_ref.event.ClienteEvent;

/**
 * Keeps the local copy of clientes in sync with the clientes service.
 */
public interface ClienteRefService {

    /**
     * Creates or updates the local cliente with the data carried by the event.
     *
     * @param event cliente event received from RabbitMQ
     */
    void updateClientRef(ClienteEvent event);
}
