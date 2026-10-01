package com.prueba.tcs.cuentas.infrastructure.exception;

import com.prueba.tcs.cuentas.infrastructure.response.ExceptionResponse;
import com.prueba.tcs.cuentas.infrastructure.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global application exception handler.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all business exceptions; status and error code come from the exception itself.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultResponse<ExceptionResponse, Void>> handleBusiness(
            BusinessException ex, HttpServletRequest request) {
        log.warn("Business exception [{}]: {} - Path: {}", ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getHttpStatus())
                .body(failure(
                        ex.getHttpStatus(),
                        ex.getErrorCode(),
                        ex.getMessage(),
                        ex.getMetadata(),
                        request.getRequestURI()));
    }

    /**
     * Unknown property in the sort parameter.
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ResultResponse<ExceptionResponse, Void>> handleInvalidSort(
            PropertyReferenceException ex, HttpServletRequest request) {
        String message = "No se puede ordenar por la propiedad '%s'".formatted(ex.getPropertyName());
        return ResponseEntity.badRequest()
                .body(failure(HttpStatus.BAD_REQUEST, "INVALID_SORT_PROPERTY", message, null, request.getRequestURI()));
    }

    /**
     * Database constraint violations not caught by the service checks.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResultResponse<ExceptionResponse, Void>> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn(
                "Data integrity violation on path {}: {}",
                request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(failure(
                        HttpStatus.CONFLICT,
                        "DATA_INTEGRITY_VIOLATION",
                        "La operacion viola una restriccion de datos",
                        null,
                        request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<ExceptionResponse, Void>> handleUnexpected(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception on path: {}", request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(failure(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "INTERNAL_ERROR",
                        "Ocurrio un error inesperado",
                        null,
                        request.getRequestURI()));
    }

    /**
     * Bean validation errors (@Valid). Messages of the same field are joined so none is lost.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> metadata = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> String.valueOf(error.getDefaultMessage()),
                        (first, second) -> first + "; " + second,
                        LinkedHashMap::new));
        return ResponseEntity.badRequest()
                .body(failure(
                        HttpStatus.BAD_REQUEST,
                        "VALIDATION_ERROR",
                        "La solicitud contiene datos invalidos",
                        metadata,
                        path(request)));
    }

    /**
     * Invalid path or query parameter type (e.g. malformed UUID or date).
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("parameter", ex.getPropertyName());
        metadata.put("value", ex.getValue());
        metadata.put(
                "requiredType",
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        String message = "Valor invalido '%s' para el parametro '%s'".formatted(ex.getValue(), ex.getPropertyName());
        return ResponseEntity.badRequest()
                .body(failure(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", message, metadata, path(request)));
    }

    /**
     * Wraps the remaining standard Spring MVC errors (400, 404, 405, 415...) in the common format.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        String message = body instanceof ProblemDetail problem && problem.getDetail() != null
                ? problem.getDetail()
                : ex.getMessage();
        return ResponseEntity.status(status)
                .headers(headers)
                .body(failure(status, status.name(), message, null, path(request)));
    }

    private ResultResponse<ExceptionResponse, Void> failure(
            HttpStatus status, String errorCode, String message, Map<String, Object> metadata, String path) {
        return ResultResponse.failure(status, new ExceptionResponse(path, metadata), null, message, errorCode);
    }

    private String path(WebRequest request) {
        return request instanceof ServletWebRequest servletRequest
                ? servletRequest.getRequest().getRequestURI()
                : null;
    }
}
