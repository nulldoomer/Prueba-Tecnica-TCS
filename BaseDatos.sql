-- Database schema of the clientes and cuentas services (PostgreSQL).
--
-- Each service owns its database and creates these tables itself with Flyway on startup
-- (src/main/resources/db/migration), so this script is only needed to build the schema by hand.
-- Do not run it on databases already used by the services: Flyway would find the tables without its history.
--
-- Run with psql, it uses \c to switch databases:
--   psql -U postgres -f BaseDatos.sql

-- =====================================================================
-- clientes service
-- =====================================================================

CREATE DATABASE clientes;

\c clientes

-- Base entity, cliente inherits from it (JOINED inheritance)
CREATE TABLE persona (
    id              UUID         PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    genero          VARCHAR(20)  NOT NULL,
    edad            SMALLINT     NOT NULL,
    identificacion  VARCHAR(20)  NOT NULL,
    direccion       VARCHAR(200) NOT NULL,
    telefono        VARCHAR(20)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    CONSTRAINT uk_persona_identificacion UNIQUE (identificacion),
    CONSTRAINT ck_persona_genero CHECK (genero IN ('MASCULINO', 'FEMENINO', 'OTRO')),
    CONSTRAINT ck_persona_edad CHECK (edad BETWEEN 0 AND 100)
);

-- contrasena is stored as a BCrypt hash, estado = false is the logical delete
CREATE TABLE cliente (
    cliente_id  UUID         PRIMARY KEY,
    contrasena  VARCHAR(100) NOT NULL,
    estado      BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cliente_persona FOREIGN KEY (cliente_id) REFERENCES persona (id) ON DELETE CASCADE
);

-- =====================================================================
-- cuentas service
-- =====================================================================

CREATE DATABASE cuentas;

\c cuentas

-- Local copy of the clientes, kept up to date with the RabbitMQ events published by clientes
CREATE TABLE cliente_ref (
    cliente_id      UUID         PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    estado          BOOLEAN      NOT NULL,
    actualizado_en  TIMESTAMP    NOT NULL
);

CREATE TABLE cuenta (
    id                UUID           PRIMARY KEY,
    numero_cuenta     VARCHAR(20)    NOT NULL,
    tipo_cuenta       VARCHAR(20)    NOT NULL,
    saldo_inicial     NUMERIC(11, 2) NOT NULL,
    saldo_disponible  NUMERIC(11, 2) NOT NULL,
    estado            BOOLEAN        NOT NULL DEFAULT TRUE,
    cliente_id        UUID           NOT NULL,
    created_at        TIMESTAMP      NOT NULL,
    updated_at        TIMESTAMP      NOT NULL,
    CONSTRAINT uk_cuenta_numero UNIQUE (numero_cuenta),
    CONSTRAINT ck_cuenta_tipo CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE')),
    CONSTRAINT ck_cuenta_saldo_inicial CHECK (saldo_inicial >= 0),
    CONSTRAINT ck_cuenta_saldo_disponible CHECK (saldo_disponible >= 0),
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente_ref (cliente_id)
);

CREATE INDEX idx_cuenta_cliente_id ON cuenta (cliente_id);

-- Immutable: valor is positive for DEPOSITO and negative for RETIRO, saldo is the balance after the movimiento
CREATE TABLE movimiento (
    id               UUID           PRIMARY KEY,
    cuenta_id        UUID           NOT NULL,
    fecha            TIMESTAMP      NOT NULL,
    tipo_movimiento  VARCHAR(20)    NOT NULL,
    valor            NUMERIC(11, 2) NOT NULL,
    saldo            NUMERIC(11, 2) NOT NULL,
    CONSTRAINT fk_movimiento_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta (id),
    CONSTRAINT ck_movimiento_tipo CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    CONSTRAINT ck_movimiento_signo CHECK (
        (tipo_movimiento = 'DEPOSITO' AND valor > 0) OR
        (tipo_movimiento = 'RETIRO'   AND valor < 0)
    ),
    CONSTRAINT ck_movimiento_saldo CHECK (saldo >= 0)
);

CREATE INDEX idx_movimiento_cuenta_fecha ON movimiento (cuenta_id, fecha);
