package com.prueba.tcs.cuentas.cuenta.service;

import com.prueba.tcs.cuentas.cuenta.dto.CuentaRequest;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaResponse;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaUpdateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use cases for managing cuentas.
 */
public interface CuentaService {

    /**
     * Opens a cuenta for an existing and active cliente.
     *
     * @param request cuenta data
     */
    CuentaResponse create(CuentaRequest request);

    /**
     * Finds a cuenta by id.
     *
     * @param id
     */
    CuentaResponse findById(UUID id);

    /**
     * Finds a cuenta by its numero de cuenta.
     *
     * @param numeroCuenta numero de cuenta
     */
    CuentaResponse findByNumeroCuenta(String numeroCuenta);

    /**
     * Returns a page of cuentas.
     *
     * @param pageable page, size and sort
     */
    Page<CuentaResponse> findAll(Pageable pageable);

    /**
     * Partially updates a cuenta.
     *
     * @param id
     * @param request
     */
    CuentaResponse update(UUID id, CuentaUpdateRequest request);

    /**
     * Logically deletes a cuenta by setting its estado to false.
     *
     * @param id
     */
    void delete(UUID id);
}
