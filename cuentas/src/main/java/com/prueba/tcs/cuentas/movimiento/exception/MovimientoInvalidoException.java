package com.prueba.tcs.cuentas.movimiento.exception;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a movimiento cannot be registered as requested.
 */
public class MovimientoInvalidoException extends BusinessException {

    public MovimientoInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "MOVIMIENTO_INVALIDO");
    }
}
