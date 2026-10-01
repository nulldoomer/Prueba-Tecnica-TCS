package com.prueba.tcs.cuentas.cuenta.validation;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;

/**
 * Data the opening rules of a cuenta need.
 *
 * @param numeroCuenta
 * @param cliente
 */
public record AperturaCuentaContext(String numeroCuenta, ClienteRefEntity cliente) {}
