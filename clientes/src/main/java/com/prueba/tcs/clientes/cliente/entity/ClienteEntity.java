package com.prueba.tcs.clientes.cliente.entity;

import com.prueba.tcs.clientes.persona.entity.PersonaEntity;
import jakarta.persistence.*;
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
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class ClienteEntity extends PersonaEntity {

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}
