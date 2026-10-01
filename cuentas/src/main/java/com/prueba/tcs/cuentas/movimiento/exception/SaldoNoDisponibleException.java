package com.prueba.tcs.cuentas.movimiento.exception;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a retiro exceeds the available balance of the cuenta.
 */
public class SaldoNoDisponibleException extends BusinessException {

    public SaldoNoDisponibleException(String numeroCuenta, BigDecimal saldoDisponible, BigDecimal valor) {
        super(
                "Saldo no disponible",
                HttpStatus.UNPROCESSABLE_CONTENT,
                "SALDO_NO_DISPONIBLE",
                Map.of(
                        "numeroCuenta", numeroCuenta,
                        "saldoDisponible", saldoDisponible,
                        "valorSolicitado", valor));
    }
}
