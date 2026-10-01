package com.prueba.tcs.cuentas.movimiento.validation;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import java.math.BigDecimal;

/**
 * Data the rules of a new movimiento need.
 *
 * @param cuenta
 * @param valor
 */
public record MovimientoContext(CuentaEntity cuenta, BigDecimal valor) {

    /**
     * Balance the cuenta would have after applying this movimiento.
     */
    public BigDecimal saldoResultante() {
        return cuenta.getSaldoDisponible().add(valor);
    }
}
