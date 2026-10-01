package com.prueba.tcs.cuentas.movimiento.validation;

import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import com.prueba.tcs.cuentas.movimiento.exception.MovimientoInvalidoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * A movimiento is either a deposito or a retiro, zero is neither.
 */
@Order(1)
@Component
class ValorDistintoDeCeroRule implements BusinessRule<MovimientoContext> {

    @Override
    public void validate(MovimientoContext context) {
        if (context.valor().signum() == 0) {
            throw new MovimientoInvalidoException("El valor del movimiento no puede ser cero");
        }
    }
}
