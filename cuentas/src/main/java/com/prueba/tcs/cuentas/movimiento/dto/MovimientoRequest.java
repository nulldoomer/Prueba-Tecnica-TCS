package com.prueba.tcs.cuentas.movimiento.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

// The type is derived from the sign of valor: positive is DEPOSITO, negative is RETIRO.
public record MovimientoRequest(
        @NotBlank(message = "El numero de cuenta es obligatorio")
        String numeroCuenta,

        @NotNull(message = "El valor es obligatorio")
        @Digits(integer = 9, fraction = 2, message = "El valor admite hasta 9 enteros y 2 decimales")
        BigDecimal valor) {}
