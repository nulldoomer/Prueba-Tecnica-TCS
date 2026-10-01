package com.prueba.tcs.cuentas.movimiento.service;

import com.prueba.tcs.cuentas.movimiento.dto.MovimientoRequest;
import com.prueba.tcs.cuentas.movimiento.dto.MovimientoResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use cases for movimientos. A movimiento is immutable once registered.
 */
public interface MovimientoService {

    /**
     * Registers a movimiento and updates the saldo of its cuenta.
     *
     * @param request
     */
    MovimientoResponse create(MovimientoRequest request);

    /**
     * Finds a movimiento by id.
     *
     * @param id
     */
    MovimientoResponse findById(UUID id);

    /**
     * Returns a page of movimientos.
     *
     * @param pageable page, size and sort
     */
    Page<MovimientoResponse> findAll(Pageable pageable);
}
