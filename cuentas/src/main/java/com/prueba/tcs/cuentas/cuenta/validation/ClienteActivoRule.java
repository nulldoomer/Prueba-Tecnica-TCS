package com.prueba.tcs.cuentas.cuenta.validation;

import com.prueba.tcs.cuentas.cliente_ref.exception.ClienteInactivoException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * An inactive cliente cannot open new cuentas.
 */
@Order(2)
@Component
class ClienteActivoRule implements BusinessRule<AperturaCuentaContext> {

    @Override
    public void validate(AperturaCuentaContext context) {
        if (!context.cliente().getEstado()) {
            throw new ClienteInactivoException(context.cliente().getClienteId());
        }
    }
}
