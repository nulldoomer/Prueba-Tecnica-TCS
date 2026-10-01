package com.prueba.tcs.cuentas.cliente_ref.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Local copy of the clientes owned by the clientes service, kept in sync through RabbitMQ events.
 * The id is assigned by the clientes service, so it is not generated here.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "cliente_ref")
public class ClienteRefEntity {

    @Id
    @Column(name = "cliente_id", nullable = false, updatable = false)
    private UUID clienteId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    /**
     * Timestamp of the last applied event.
     */
    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}
