package com.prueba.tcs.cuentas.movimiento;

import java.math.BigDecimal;

public enum TipoMovimiento {
    DEPOSITO,
    RETIRO;

    /**
     * Converts the requested amount into the signed valor stored: negative for a retiro.
     *
     * @param monto
     */
    public BigDecimal aplicarSigno(BigDecimal monto) {
        return this == RETIRO ? monto.negate() : monto;
    }
}
