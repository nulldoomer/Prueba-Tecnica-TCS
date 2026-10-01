package com.prueba.tcs.clientes.cliente.dto;

import com.prueba.tcs.clientes.persona.Genero;
import jakarta.validation.constraints.*;

public record ClienteRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @NotNull(message = "El genero es obligatorio")
        Genero genero,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 100, message = "La edad no puede ser mayor a 100")
        Integer edad,

        @NotBlank(message = "La identificacion es obligatoria")
        @Size(max = 20, message = "La identificacion no puede superar 20 caracteres")
        String identificacion,

        @NotBlank(message = "La direccion es obligatoria")
        @Size(max = 200, message = "La direccion no puede superar 200 caracteres")
        String direccion,

        @NotBlank(message = "El telefono es obligatorio")
        @Size(max = 20, message = "El telefono no puede superar 20 caracteres")
        String telefono,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 4, max = 30, message = "La contrasena debe tener entre 4 y 30 caracteres")
        String contrasena,

        Boolean estado
) {
}
