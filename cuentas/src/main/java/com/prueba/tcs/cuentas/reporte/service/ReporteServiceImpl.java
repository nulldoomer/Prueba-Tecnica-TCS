package com.prueba.tcs.cuentas.reporte.service;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cliente_ref.repository.ClienteRefRepository;
import com.prueba.tcs.cuentas.cuenta.repository.CuentaRepository;
import com.prueba.tcs.cuentas.infrastructure.exception.ResourceNotFoundException;
import com.prueba.tcs.cuentas.reporte.RangoFechas;
import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse;
import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse.CuentaReporte;
import com.prueba.tcs.cuentas.reporte.mapper.ReporteMapper;
import com.prueba.tcs.cuentas.reporte.repository.ReporteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ReporteServiceImpl implements ReporteService {

    private final ClienteRefRepository clienteRefRepository;
    private final CuentaRepository cuentaRepository;
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    @Override
    public EstadoCuentaResponse generarEstadoCuenta(UUID clienteId, List<LocalDate> fecha) {

        RangoFechas rango = RangoFechas.of(fecha);

        ClienteRefEntity cliente = clienteRefRepository
                .findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id " + clienteId + " no encontrado"));

        // One query per cuenta, a cuenta without movimientos in the range gets an empty list
        List<CuentaReporte> cuentas = cuentaRepository.findByClienteClienteIdOrderByNumeroCuenta(clienteId)
                .stream()
                .map(cuenta -> reporteMapper.toCuentaReporte(
                        cuenta, reporteRepository.findMovimientos(cuenta, rango.inicio(), rango.endCompleteDay())))
                .toList();

        return new EstadoCuentaResponse(clienteId, cliente.getNombre(), rango.desde(), rango.hasta(), cuentas);
    }
}
