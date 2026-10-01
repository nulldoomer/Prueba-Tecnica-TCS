package com.prueba.tcs.cuentas.movimiento.validation;

import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import com.prueba.tcs.cuentas.movimiento.exception.SaldoNoDisponibleException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The retiro cannot leave the cuenta with a negative balance.
 */
@Order(3)
@Component
class SaldoDisponibleRule implements BusinessRule<MovimientoContext> {

    @Override
    public void validate(MovimientoContext context) {
        if (context.saldoResultante().signum() < 0) {
            throw new SaldoNoDisponibleException(
                    context.cuenta().getNumeroCuenta(), context.cuenta().getSaldoDisponible(), context.valor());
        }
    }
}
