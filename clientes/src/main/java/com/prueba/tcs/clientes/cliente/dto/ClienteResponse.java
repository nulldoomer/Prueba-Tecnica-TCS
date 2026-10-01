package com.prueba.tcs.clientes.cliente.dto;

import com.prueba.tcs.clientes.persona.Genero;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID clienteId,
        String nombre,
        Genero genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        Boolean estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
