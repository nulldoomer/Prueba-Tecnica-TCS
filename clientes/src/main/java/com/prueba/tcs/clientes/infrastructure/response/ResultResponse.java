package com.prueba.tcs.clientes.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard envelope for every API response, successful or not.
 *
 * @param <T> type of the result payload
 * @param <E> type of each error detail (e.g. a field validation error)
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse<T, E> {
    private final int code;
    private final boolean isSuccess;
    private final T result;
    private final List<E> errors;
    private final String message;
    private final String messageCode;
    private final LocalDateTime timestamp;

    private ResultResponse(int code, boolean isSuccess, T result, List<E> errors, String message, String messageCode) {
        this.code = code;
        this.isSuccess = isSuccess;
        this.result = result;
        this.errors = errors == null ? null : List.copyOf(errors);
        this.message = message;
        this.messageCode = messageCode;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a successful response with default message and 200 status.
     *
     * @param result The result data
     * @return A success response
     */
    public static <T, E> ResultResponse<T, E> success(T result) {
        return success(HttpStatus.OK, result, "Operation successful", "OPERATION_SUCCESS");
    }

    /**
     * Successful response with a custom message and 200 status
     */
    public static <T, E> ResultResponse<T, E> success(T result, String message, String messageCode) {
        return success(HttpStatus.OK, result, message, messageCode);
    }

    /**
     * Successful response with full control over message and status.
     */
    public static <T, E> ResultResponse<T, E> success(HttpStatus status, T result, String message, String messageCode) {
        return new ResultResponse<>(status.value(), true, result, null, message, messageCode);
    }

    /**
     * Successful creation with 201 status, for POST endpoints.
     */
    public static <T, E> ResultResponse<T, E> created(T result) {
        return success(HttpStatus.CREATED, result, "Resource created", "RESOURCE_CREATED");
    }

    /**
     * Failed response without details (e.g. not found, duplicate, insufficient balance).
     */
    public static <T, E> ResultResponse<T, E> failure(HttpStatus status, String message, String messageCode) {
        return failure(status, null, message, messageCode);
    }

    /**
     * Failed response with a list of error details (e.g. bean validation errors).
     */
    public static <T, E> ResultResponse<T, E> failure(HttpStatus status, List<E> errors, String message, String messageCode) {
        return failure(status, null, errors, message, messageCode);
    }

    /**
     * Failed response carrying an error detail object as result (e.g. {@code ExceptionResponse}).
     */
    public static <T, E> ResultResponse<T, E> failure(HttpStatus status, T result, List<E> errors, String message, String messageCode) {
        return new ResultResponse<>(status.value(), false, result, errors, message, messageCode);
    }
}
