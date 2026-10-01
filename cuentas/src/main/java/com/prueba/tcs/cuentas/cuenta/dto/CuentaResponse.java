package com.prueba.tcs.cuentas.cuenta.dto;

import com.prueba.tcs.cuentas.cuenta.TipoCuenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CuentaResponse(
        UUID cuentaId,
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado,
        UUID clienteId,
        String clienteNombre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
