-- Create table cliente inherits from persona
CREATE TABLE cliente (
    cliente_id  UUID         PRIMARY KEY,
    contrasena  VARCHAR(100) NOT NULL,
    estado      BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cliente_persona FOREIGN KEY (cliente_id) REFERENCES persona (id) ON DELETE CASCADE
);