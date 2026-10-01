package com.prueba.tcs.cuentas.movimiento.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;
import com.prueba.tcs.cuentas.movimiento.exception.SaldoNoDisponibleException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MovimientoRulesTest {

    private final SaldoDisponibleRule saldoDisponibleRule = new SaldoDisponibleRule();

    @Test
    void retiroMayorAlSaldoLanzaSaldoNoDisponible() {
        MovimientoContext context = new MovimientoContext(cuentaConSaldo("540.00"), new BigDecimal("-575.00"));

        SaldoNoDisponibleException ex =
                assertThrows(SaldoNoDisponibleException.class, () -> saldoDisponibleRule.validate(context));
        assertEquals("Saldo no disponible", ex.getMessage());
    }

    @Test
    void retiroIgualAlSaldoDejaLaCuentaEnCero() {
        MovimientoContext context = new MovimientoContext(cuentaConSaldo("540.00"), new BigDecimal("-540.00"));

        assertDoesNotThrow(() -> saldoDisponibleRule.validate(context));
        assertEquals(0, context.saldoResultante().signum());
    }

    @Test
    void depositoEnCuentaSinSaldoEsValido() {
        MovimientoContext context = new MovimientoContext(cuentaConSaldo("0.00"), new BigDecimal("150.00"));

        assertDoesNotThrow(() -> saldoDisponibleRule.validate(context));
    }

    @Test
    void retiroSeRegistraConValorNegativoYDepositoConPositivo() {
        assertEquals(new BigDecimal("-575"), TipoMovimiento.RETIRO.aplicarSigno(new BigDecimal("575")));
        assertEquals(new BigDecimal("600"), TipoMovimiento.DEPOSITO.aplicarSigno(new BigDecimal("600")));
    }

    private static CuentaEntity cuentaConSaldo(String saldo) {
        return CuentaEntity.builder()
                .numeroCuenta("496825")
                .saldoDisponible(new BigDecimal(saldo))
                .build();
    }
}
