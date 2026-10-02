package com.prueba.tcs.clientes.cliente.service;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.dto.ClienteUpdateRequest;
import com.prueba.tcs.clientes.cliente.entity.ClienteEntity;
import com.prueba.tcs.clientes.cliente.event.ClienteEvent;
import com.prueba.tcs.clientes.cliente.mapper.ClienteMapper;
import com.prueba.tcs.clientes.cliente.repository.ClienteRepository;
import com.prueba.tcs.clientes.infrastructure.exception.DuplicateResourceException;
import com.prueba.tcs.clientes.infrastructure.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    // =================
    // ----- CRUD ------
    // =================

    @Override
    @Transactional
    public ClienteResponse create(ClienteRequest request) {

        validateIdentificacionAvailable(request.identificacion());

        ClienteEntity cliente = clienteMapper.toEntity(request);

        if (cliente.getEstado() == null) {
            cliente.setEstado(true);
        }

        cliente.setContrasena(passwordEncoder.encode(request.contrasena()));

        ClienteEntity saved = clienteRepository.save(cliente);
        eventPublisher.publishEvent(ClienteEvent.of(ClienteEvent.Type.CREATED, saved));

        return clienteMapper.toResponse(saved);
    }

    @Override
    public ClienteResponse findById(UUID id) {

        return clienteMapper.toResponse(getCliente(id));
    }

    @Override
    public ClienteResponse findByIdentificacion(String identificacion) {

        return clienteRepository
                .findByIdentificacion(identificacion)
                .map(clienteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con identificacion " + identificacion + " no encontrado"));
    }

    @Override
    public Page<ClienteResponse> findAll(Boolean estado, Pageable pageable) {

        Page<ClienteEntity> clientes =
                estado == null ? clienteRepository.findAll(pageable) : clienteRepository.findByEstado(estado, pageable);

        return clientes.map(clienteMapper::toResponse);
    }

    @Override
    @Transactional
    public ClienteResponse replace(UUID id, ClienteRequest request) {

        ClienteEntity cliente = getCliente(id);

        if (!cliente.getIdentificacion().equals(request.identificacion())) {
            validateIdentificacionAvailable(request.identificacion());
        }

        clienteMapper.replaceEntity(request, cliente);
        cliente.setContrasena(passwordEncoder.encode(request.contrasena()));

        ClienteEntity saved = clienteRepository.saveAndFlush(cliente);
        eventPublisher.publishEvent(ClienteEvent.of(ClienteEvent.Type.UPDATED, saved));

        return clienteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClienteResponse update(UUID id, ClienteUpdateRequest request) {

        ClienteEntity cliente = getCliente(id);
        clienteMapper.updateEntity(request, cliente);

        if (request.contrasena() != null) {

            cliente.setContrasena(passwordEncoder.encode(request.contrasena()));
        }
        // Flush so @LastModifiedDate is applied before building the response
        ClienteEntity saved = clienteRepository.saveAndFlush(cliente);
        eventPublisher.publishEvent(ClienteEvent.of(ClienteEvent.Type.UPDATED, saved));

        return clienteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClienteResponse activateCliente(UUID id) {

        ClienteEntity entity = getCliente(id);
        entity.setEstado(true);

        ClienteEntity updated = clienteRepository.saveAndFlush(entity);
        eventPublisher.publishEvent(ClienteEvent.of(ClienteEvent.Type.UPDATED, updated));

        return clienteMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ClienteResponse deactivateCliente(UUID id) {

        ClienteEntity entity = getCliente(id);
        entity.setEstado(false);

        // Logical delete: the record is kept to have historic data.
        ClienteEntity updated = clienteRepository.saveAndFlush(entity);
        eventPublisher.publishEvent(ClienteEvent.of(ClienteEvent.Type.DEACTIVATED, updated));

        return clienteMapper.toResponse(updated);
    }

    // TODO: Implementar busquedas personalizadas por campos

    // =====================
    // ----- HELPERS --------
    // =====================

    private void validateIdentificacionAvailable(String identificacion) {

        if (clienteRepository.existsByIdentificacion(identificacion)) {
            throw new DuplicateResourceException("Ya existe un cliente con identificacion " + identificacion);
        }
    }

    private ClienteEntity getCliente(UUID id) {

        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id " + id + " no encontrado"));
    }
}
