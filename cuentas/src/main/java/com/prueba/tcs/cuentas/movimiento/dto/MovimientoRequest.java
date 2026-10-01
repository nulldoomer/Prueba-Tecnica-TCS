package com.prueba.tcs.cuentas.movimiento.dto;

import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

// valor is always positive; the backend applies the sign from tipoMovimiento.
public record MovimientoRequest(
        @NotBlank(message = "El numero de cuenta es obligatorio")
        String numeroCuenta,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @NotNull(message = "El valor es obligatorio")
        @Positive(message = "El valor debe ser mayor a cero")
        @Digits(integer = 9, fraction = 2, message = "El valor admite hasta 9 enteros y 2 decimales")
        BigDecimal valor) {}
