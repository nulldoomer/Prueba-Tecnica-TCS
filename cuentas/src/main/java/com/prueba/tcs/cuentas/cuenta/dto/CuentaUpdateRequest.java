package com.prueba.tcs.cuentas.cuenta.dto;

import com.prueba.tcs.cuentas.cuenta.TipoCuenta;

// Partial update. numeroCuenta, saldos and cliente are not editable once the cuenta exists.
public record CuentaUpdateRequest(TipoCuenta tipoCuenta, Boolean estado) {}
