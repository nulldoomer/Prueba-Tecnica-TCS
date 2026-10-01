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
