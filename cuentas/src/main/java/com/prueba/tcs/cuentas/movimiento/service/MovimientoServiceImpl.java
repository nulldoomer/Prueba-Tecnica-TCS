package com.prueba.tcs.cuentas.movimiento.service;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.cuenta.repository.CuentaRepository;
import com.prueba.tcs.cuentas.infrastructure.exception.ResourceNotFoundException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
import com.prueba.tcs.cuentas.movimiento.TipoMovimiento;
import com.prueba.tcs.cuentas.movimiento.dto.MovimientoRequest;
import com.prueba.tcs.cuentas.movimiento.dto.MovimientoResponse;
import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import com.prueba.tcs.cuentas.movimiento.mapper.MovimientoMapper;
import com.prueba.tcs.cuentas.movimiento.repository.MovimientoRepository;
import com.prueba.tcs.cuentas.movimiento.validation.MovimientoContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MovimientoServiceImpl implements MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final List<BusinessRule<MovimientoContext>> movimientoRules;

    @Override
    @Transactional
    public MovimientoResponse create(MovimientoRequest request) {

        CuentaEntity cuenta = cuentaRepository
                .findByNumeroCuenta(request.numeroCuenta())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cuenta " + request.numeroCuenta() + " no encontrada"));

        MovimientoContext context = new MovimientoContext(cuenta, request.valor());
        movimientoRules.forEach(rule -> rule.validate(context));

        // Saldo update and movimiento insert commit together in this transaction
        BigDecimal saldo = context.saldoResultante();
        cuenta.setSaldoDisponible(saldo);

        MovimientoEntity movimiento = MovimientoEntity.builder()
                .cuenta(cuenta)
                .fecha(LocalDateTime.now())
                .tipoMovimiento(request.valor().signum() > 0 ? TipoMovimiento.DEPOSITO : TipoMovimiento.RETIRO)
                .valor(request.valor())
                .saldo(saldo)
                .build();

        return movimientoMapper.toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    public MovimientoResponse findById(UUID id) {
        return movimientoRepository
                .findById(id)
                .map(movimientoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento con id " + id + " no encontrado"));
    }

    @Override
    public Page<MovimientoResponse> findAll(Pageable pageable) {
        return movimientoRepository.findAll(pageable).map(movimientoMapper::toResponse);
    }
}
