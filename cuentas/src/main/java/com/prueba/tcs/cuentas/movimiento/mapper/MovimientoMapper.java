package com.prueba.tcs.cuentas.movimiento.mapper;

import com.prueba.tcs.cuentas.movimiento.dto.MovimientoResponse;
import com.prueba.tcs.cuentas.movimiento.entity.MovimientoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Only maps entity to response: a movimiento is built by the service, which computes its tipo, fecha and saldo.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovimientoMapper {

    @Mapping(target = "movimientoId", source = "id")
    @Mapping(target = "numeroCuenta", source = "cuenta.numeroCuenta")
    MovimientoResponse toResponse(MovimientoEntity entity);
}
