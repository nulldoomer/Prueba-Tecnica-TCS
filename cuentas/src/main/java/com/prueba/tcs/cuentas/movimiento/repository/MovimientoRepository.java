package com.prueba.tcs.cuentas.movimiento.repository;

import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, UUID> {

    /**
     * Fetches each movimiento with its cuenta in a single query.
     *
     * @param pageable
     */
    @Override
    @Query(
            value = "select m from MovimientoEntity m join fetch m.cuenta",
            countQuery = "select count(m) from MovimientoEntity m")
    Page<MovimientoEntity> findAll(Pageable pageable);
}
