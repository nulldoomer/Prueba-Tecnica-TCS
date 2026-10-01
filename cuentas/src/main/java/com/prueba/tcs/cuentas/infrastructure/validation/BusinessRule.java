package com.prueba.tcs.cuentas.infrastructure.validation;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;

/**
 * A single business rule over a target. Rules of the same target are injected
 * as an ordered and run one after another; the first violated rule throws and
 * stops the chain.
 *
 * @param <T> what the rule validates
 */
@FunctionalInterface
public interface BusinessRule<T> {

    /**
     * @param target data to validate
     * @throws BusinessException if the rule is violated
     */
    void validate(T target);
}
