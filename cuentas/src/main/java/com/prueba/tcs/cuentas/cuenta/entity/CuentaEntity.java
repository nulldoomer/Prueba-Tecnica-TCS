package com.prueba.tcs.cuentas.cuenta.entity;

import com.prueba.tcs.cuentas.auditable.Auditable;
import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cuenta.TipoCuenta;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cuenta")
public class CuentaEntity extends Auditable {

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private TipoCuenta tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 11, scale = 2)
    private BigDecimal saldoInicial;

    /**
     * Current balance, updated on every movimiento.
     */
    @Column(name = "saldo_disponible", nullable = false, precision = 11, scale = 2)
    private BigDecimal saldoDisponible;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteRefEntity cliente;
}
