package com.prueba.tcs.cuentas.cuenta.mapper;

import com.prueba.tcs.cuentas.cuenta.dto.CuentaRequest;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaResponse;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaUpdateRequest;
import com.prueba.tcs.cuentas.cuenta.entity.CuentaEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CuentaMapper {

    /**
     * The cliente is resolved and assigned by the service; a new cuenta starts with its saldo inicial as available.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "saldoDisponible", source = "saldoInicial")
    @Mapping(target = "estado", defaultValue = "true")
    CuentaEntity toEntity(CuentaRequest request);

    @Mapping(target = "cuentaId", source = "id")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "clienteNombre", source = "cliente.nombre")
    CuentaResponse toResponse(CuentaEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "numeroCuenta", ignore = true)
    @Mapping(target = "saldoInicial", ignore = true)
    @Mapping(target = "saldoDisponible", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    void updateEntity(CuentaUpdateRequest request, @MappingTarget CuentaEntity entity);
}
