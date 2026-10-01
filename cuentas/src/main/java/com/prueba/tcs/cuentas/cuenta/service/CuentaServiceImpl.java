package com.prueba.tcs.cuentas.cuenta.service;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cliente_ref.repository.ClienteRefRepository;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaRequest;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaResponse;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaUpdateRequest;
import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.cuenta.mapper.CuentaMapper;
import com.prueba.tcs.cuentas.cuenta.repository.CuentaRepository;
import com.prueba.tcs.cuentas.cuenta.validation.AperturaCuentaContext;
import com.prueba.tcs.cuentas.infrastructure.exception.ResourceNotFoundException;
import com.prueba.tcs.cuentas.infrastructure.validation.BusinessRule;
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
class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRefRepository clienteRefRepository;
    private final CuentaMapper cuentaMapper;
    private final List<BusinessRule<AperturaCuentaContext>> aperturaRules;

    @Override
    @Transactional
    public CuentaResponse create(CuentaRequest request) {

        ClienteRefEntity cliente = clienteRefRepository
                .findById(request.clienteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente con id " + request.clienteId() + " no encontrado"));

        AperturaCuentaContext context = new AperturaCuentaContext(request.numeroCuenta(), cliente);
        aperturaRules.forEach(rule -> rule.validate(context));

        CuentaEntity cuenta = cuentaMapper.toEntity(request);
        cuenta.setCliente(cliente);

        return cuentaMapper.toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    public CuentaResponse findById(UUID id) {
        return cuentaMapper.toResponse(getCuenta(id));
    }

    @Override
    public CuentaResponse findByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository
                .findByNumeroCuenta(numeroCuenta)
                .map(cuentaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta " + numeroCuenta + " no encontrada"));
    }

    @Override
    public Page<CuentaResponse> findAll(Pageable pageable) {
        return cuentaRepository.findAll(pageable).map(cuentaMapper::toResponse);
    }

    @Override
    @Transactional
    public CuentaResponse update(UUID id, CuentaUpdateRequest request) {

        CuentaEntity cuenta = getCuenta(id);
        cuentaMapper.updateEntity(request, cuenta);

        // Flush so @LastModifiedDate is applied before building the response
        return cuentaMapper.toResponse(cuentaRepository.saveAndFlush(cuenta));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        // Logical delete so movimientos keep referencing the cuenta
        getCuenta(id).setEstado(false);
    }

    private CuentaEntity getCuenta(UUID id) {
        return cuentaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con id " + id + " no encontrada"));
    }
}
