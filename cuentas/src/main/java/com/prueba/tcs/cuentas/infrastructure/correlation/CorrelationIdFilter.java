package com.prueba.tcs.cuentas.infrastructure.correlation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Assigns a correlation id to every request so its logs can be traced
 * across services, reuses the incoming X-Correlation-Id header when valid
 * otherwise generates one.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    // AMQP limits the message correlationId to 255 bytes
    private static final int MAX_LENGTH = 64;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain
    ) throws ServletException, IOException {

        String incoming = request.getHeader(HEADER);

        String correlationId = incoming !=
                null && !incoming.isBlank() && incoming.length() <= MAX_LENGTH
                ? incoming
                : UUID.randomUUID().toString();

        MDC.put(MDC_KEY, correlationId);

        response.setHeader(HEADER, correlationId);

        try {
            chain.doFilter(request, response);

        } finally {

            MDC.remove(MDC_KEY);
        }
    }
}
