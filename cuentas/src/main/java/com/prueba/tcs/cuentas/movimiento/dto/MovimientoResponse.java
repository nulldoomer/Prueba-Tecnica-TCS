package com.prueba.tcs.cuentas.movimiento.dto;

import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientoResponse(
        UUID movimientoId,
        String numeroCuenta,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo
) {
}
