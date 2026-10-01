-- Create table persona
CREATE TABLE persona (
    id              UUID         PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    genero          VARCHAR(20)  NOT NULL,
    edad            SMALLINT     NOT NULL,
    identificacion  VARCHAR(20)  NOT NULL,
    direccion       VARCHAR(200) NOT NULL,
    telefono        VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    CONSTRAINT uk_persona_identificacion UNIQUE (identificacion),
    CONSTRAINT ck_persona_genero CHECK (genero IN ('MASCULINO', 'FEMENINO', 'OTRO')),
    CONSTRAINT ck_persona_edad CHECK (edad BETWEEN 0 AND 100)
);