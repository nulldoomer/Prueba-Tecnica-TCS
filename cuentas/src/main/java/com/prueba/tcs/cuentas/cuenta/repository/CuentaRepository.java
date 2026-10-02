package com.prueba.tcs.cuentas.cuenta.repository;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CuentaRepository extends JpaRepository<CuentaEntity, UUID> {

    /**
     * Fetches each cuenta with its cliente in a single query.
     *
     * @param pageable
     */
    @Override
    @Query(
            value = "select c from CuentaEntity c join fetch c.cliente",
            countQuery = "select count(c) from CuentaEntity c")
    Page<CuentaEntity> findAll(Pageable pageable);

    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    boolean existsByNumeroCuenta(String numeroCuenta);

    List<CuentaEntity> findByClienteClienteIdOrderByNumeroCuenta(UUID clienteId);
}
