package com.prueba.tcs.cuentas.reporte.service;

import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Use cases for reportes.
 */
public interface ReporteService {

    /**
     * Builds the estado de cuenta of a cliente for one date or a range of two.
     *
     * @param clienteId
     * @param fecha
     */
    EstadoCuentaResponse generarEstadoCuenta(UUID clienteId, List<LocalDate> fecha);
}
