package com.prueba.tcs.clientes.cliente.repository;

import com.prueba.tcs.clientes.cliente.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);
}
