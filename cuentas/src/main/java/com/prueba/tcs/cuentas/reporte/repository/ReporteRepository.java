package com.prueba.tcs.cuentas.reporte.repository;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ReporteRepository extends Repository<MovimientoEntity, UUID> {

    /**
     * Movimientos of a cuenta in the range, oldest first.
     *
     * @param cuenta
     * @param inicio
     * @param fin
     */
    @Query("select m from MovimientoEntity m where m.cuenta = :cuenta"
            + " and m.fecha >= :inicio and m.fecha < :fin order by m.fecha")
    List<MovimientoEntity> findMovimientos(CuentaEntity cuenta, LocalDateTime inicio, LocalDateTime fin);
}
