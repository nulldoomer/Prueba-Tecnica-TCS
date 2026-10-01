package com.prueba.tcs.cuentas.cliente_ref.repository;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRefRepository extends JpaRepository<ClienteRefEntity, UUID> {}
