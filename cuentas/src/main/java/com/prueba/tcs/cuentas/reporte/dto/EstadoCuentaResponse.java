package com.prueba.tcs.cuentas.reporte.dto;

import com.prueba.tcs.cuentas.cuenta.TipoCuenta;
import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EstadoCuentaResponse(
        UUID clienteId,
        String cliente,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<CuentaReporte> cuentas
) {

    public record CuentaReporte(
            String numeroCuenta,
            TipoCuenta tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            Boolean estado,
            List<MovimientoReporte> movimientos) {}

    public record MovimientoReporte(
            LocalDateTime fecha,
            TipoMovimiento tipoMovimiento,
            BigDecimal saldoInicial,
            BigDecimal valor,
            BigDecimal saldo) {}
}
