package com.prueba.tcs.cuentas.movimiento.repository;

import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, UUID> {}
