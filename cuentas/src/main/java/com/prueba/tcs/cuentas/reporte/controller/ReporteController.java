package com.prueba.tcs.cuentas.reporte.controller;

import com.prueba.tcs.cuentas.infrastructure.response.ResultResponse;
import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse;
import com.prueba.tcs.cuentas.reporte.service.ReporteService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for reportes.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Estado de cuenta para cliente
     *
     * @param clienteId
     * @param fecha
     */
    @GetMapping
    public ResponseEntity<ResultResponse<EstadoCuentaResponse, String>> estadoCuenta(
            @RequestParam UUID clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> fecha) {

        return ResponseEntity.ok(ResultResponse.success(reporteService.generarEstadoCuenta(clienteId, fecha)));
    }
}
