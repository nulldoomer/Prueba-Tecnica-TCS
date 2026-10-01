package com.prueba.tcs.cuentas.cuenta.dto;

import com.prueba.tcs.cuentas.cuenta.TipoCuenta;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CuentaRequest(

        @NotBlank(message = "El numero de cuenta es obligatorio")
        @Size(max = 20, message = "El numero de cuenta no puede superar 20 caracteres")
        @Pattern(regexp = "\\d+", message = "El numero de cuenta solo puede contener digitos")
        String numeroCuenta,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        TipoCuenta tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio")
        @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
        @Digits(integer = 9, fraction = 2, message = "El saldo inicial admite hasta 9 enteros y 2 decimales")
        BigDecimal saldoInicial,

        Boolean estado,

        @NotNull(message = "El cliente es obligatorio")
        UUID clienteId
) {
}
