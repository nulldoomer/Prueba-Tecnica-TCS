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
