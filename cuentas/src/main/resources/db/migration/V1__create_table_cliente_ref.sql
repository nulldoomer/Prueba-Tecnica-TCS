-- Create table cuenta
CREATE TABLE cliente_ref (
    cliente_id      UUID         PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    estado          BOOLEAN      NOT NULL,
    actualizado_en  TIMESTAMP    NOT NULL
);