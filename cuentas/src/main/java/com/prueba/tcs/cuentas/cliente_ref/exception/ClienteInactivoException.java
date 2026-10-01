package com.prueba.tcs.cuentas.cliente_ref.exception;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Thrown when opening a cuenta for a cliente whose estado is false.
 */
public class ClienteInactivoException extends BusinessException {

    public ClienteInactivoException(UUID clienteId) {
        super("El cliente " + clienteId + " esta inactivo", HttpStatus.UNPROCESSABLE_CONTENT, "CLIENTE_INACTIVO");
    }
}
