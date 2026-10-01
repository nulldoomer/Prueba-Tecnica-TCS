package com.prueba.tcs.clientes.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/**
 * Error detail returned when a request fails.
 *
 * @param path     request URI that produced the error
 * @param metadata extra context
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(String path, Map<String, Object> metadata) {}
