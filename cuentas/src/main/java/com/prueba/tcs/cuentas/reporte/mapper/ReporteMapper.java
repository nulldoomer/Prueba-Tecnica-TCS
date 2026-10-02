package com.prueba.tcs.cuentas.reporte.mapper;

import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse.CuentaReporte;
import com.prueba.tcs.cuentas.reporte.dto.EstadoCuentaResponse.MovimientoReporte;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReporteMapper {

    /**
     * valor is signed, so the saldo before the movimiento is saldo - valor.
     *
     * @param movimiento
     */
    @Mapping(target = "saldoInicial", expression = "java(movimiento.getSaldo().subtract(movimiento.getValor()))")
    MovimientoReporte toMovimientoReporte(MovimientoEntity movimiento);

    /**
     * Each movimiento is mapped with toMovimientoReporte.
     *
     * @param cuenta
     * @param movimientos
     */
    @Mapping(target = "movimientos", source = "movimientos")
    CuentaReporte toCuentaReporte(CuentaEntity cuenta, List<MovimientoEntity> movimientos);
}
