package com.prueba.tcs.cuentas.movimiento.entity;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable record of a transaction, because it can't be deleted or updated to
 * maintain consistency.
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "movimiento")
public class MovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_id", nullable = false, updatable = false)
    private CuentaEntity cuenta;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, updatable = false, length = 20)
    private TipoMovimiento tipoMovimiento;

    /**
     * Positive for DEPOSITO, negative for RETIRO.
     */
    @Column(name = "valor", nullable = false, updatable = false, precision = 11, scale = 2)
    private BigDecimal valor;

    /**
     * Account balance right after this movimiento.
     */
    @Column(name = "saldo", nullable = false, updatable = false, precision = 11, scale = 2)
    private BigDecimal saldo;
}
