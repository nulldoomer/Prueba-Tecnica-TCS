package com.prueba.tcs.cuentas.cliente_ref.service;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cliente_ref.event.ClienteEvent;
import com.prueba.tcs.cuentas.cliente_ref.repository.ClienteRefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class ClienteRefServiceImpl implements ClienteRefService {

    private final ClienteRefRepository clienteRefRepository;

    @Override
    @Transactional
    public void updateClientRef(ClienteEvent event) {

        Optional<ClienteRefEntity> existing = clienteRefRepository.findById(event.clienteId());

        if (existing.isPresent() &&
                existing.get().getActualizadoEn().isAfter(event.occurredAt())
        ) {

            log.warn("Ignoring stale {} event for cliente {}", event.eventType(),
                    event.clienteId()
            );

            return;
        }

        ClienteRefEntity cliente = existing.orElseGet(() -> ClienteRefEntity.builder()
                .clienteId(event.clienteId())
                .build());

        cliente.setNombre(event.nombre());
        cliente.setEstado(event.estado());
        cliente.setActualizadoEn(event.occurredAt());

        clienteRefRepository.save(cliente);
    }
}
