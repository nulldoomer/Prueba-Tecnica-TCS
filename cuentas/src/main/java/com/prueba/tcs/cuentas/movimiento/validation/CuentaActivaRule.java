package com.prueba.tcs.cuentas.movimiento.validation;

import com.prueba.tcs.cuentas.cuenta.exception.CuentaInactivaException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * An inactive cuenta cannot receive movimientos.
 */
@Order(1)
@Component
class CuentaActivaRule implements BusinessRule<MovimientoContext> {

    @Override
    public void validate(MovimientoContext context) {
        if (!context.cuenta().getEstado()) {
            throw new CuentaInactivaException(context.cuenta().getNumeroCuenta());
        }
    }
}
