package com.prueba.tcs.cuentas.cliente_ref.repository;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRefRepository extends JpaRepository<ClienteRefEntity, UUID> {
}
