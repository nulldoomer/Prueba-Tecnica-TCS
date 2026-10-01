package com.prueba.tcs.cuentas.cuenta.validation;

import com.prueba.tcs.cuentas.cuenta.repository.CuentaRepository;
import com.prueba.tcs.cuentas.infrastructure.exception.DuplicateResourceException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The numero de cuenta is the business key of a cuenta, so it cannot be repeated.
 */
@Order(1)
@Component
@RequiredArgsConstructor
class NumeroCuentaUnicoRule implements BusinessRule<AperturaCuentaContext> {

    private final CuentaRepository cuentaRepository;

    @Override
    public void validate(AperturaCuentaContext context) {
        if (cuentaRepository.existsByNumeroCuenta(context.numeroCuenta())) {
            throw new DuplicateResourceException("Ya existe una cuenta con numero " + context.numeroCuenta());
        }
    }
}
