package com.prueba.tcs.clientes.cliente.dto;

import com.prueba.tcs.clientes.persona.Genero;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

// Partial update
public record ClienteUpdateRequest(

        @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
        String nombre,

        Genero genero,

        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 100, message = "La edad no puede ser mayor a 100")
        Integer edad,

        @Size(min = 1, max = 200, message = "La direccion debe tener entre 1 y 200 caracteres")
        String direccion,

        @Size(min = 1, max = 20, message = "El telefono debe tener entre 1 y 20 caracteres")
        String telefono,

        @Size(min = 4, max = 30, message = "La contrasena debe tener entre 4 y 30 caracteres")
        String contrasena,

        Boolean estado
) {
}
