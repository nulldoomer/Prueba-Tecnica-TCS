package com.prueba.tcs.cuentas.infrastructure.exception;

import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for business rule violations. Each subclass defines its HTTP status and error code,
 * so a single handler can build the error response for all of them.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final Map<String, Object> metadata;

    protected BusinessException(String message, HttpStatus httpStatus, String errorCode) {
        this(message, httpStatus, errorCode, null);
    }

    protected BusinessException(String message, HttpStatus httpStatus, String errorCode, Map<String, Object> metadata) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.metadata = metadata == null ? null : Map.copyOf(metadata);
    }
}
