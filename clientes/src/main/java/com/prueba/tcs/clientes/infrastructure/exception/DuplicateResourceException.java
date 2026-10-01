package com.prueba.tcs.clientes.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when creating a resource that violates a uniqueness rule.
 */
public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }
}
