package com.prueba.tcs.cuentas.cuenta.exception;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when registering a movimiento on a cuenta with false estado.
 */
public class CuentaInactivaException extends BusinessException {

    public CuentaInactivaException(String numeroCuenta) {
        super("La cuenta " + numeroCuenta + " esta inactiva", HttpStatus.UNPROCESSABLE_CONTENT, "CUENTA_INACTIVA");
    }
}
