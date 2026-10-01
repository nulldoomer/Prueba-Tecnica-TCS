package com.prueba.tcs.cuentas.movimiento.validation;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cliente_ref.exception.ClienteInactivoException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The cuentas of an inactive cliente cannot operate, reads the lazy cliente,
 * so it must run inside the service transaction.
 */
@Order(2)
@Component
class PropietarioActivoRule implements BusinessRule<MovimientoContext> {

    @Override
    public void validate(MovimientoContext context) {
        ClienteRefEntity cliente = context.cuenta().getCliente();
        if (!cliente.getEstado()) {
            throw new ClienteInactivoException(cliente.getClienteId());
        }
    }
}
