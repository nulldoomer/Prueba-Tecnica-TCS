package com.prueba.tcs.clientes.cliente.mapper;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.dto.ClienteUpdateRequest;
import com.prueba.tcs.clientes.cliente.entity.ClienteEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estado", defaultValue = "true")
    ClienteEntity toEntity(ClienteRequest request);

    @Mapping(target = "clienteId", source = "id")
    ClienteResponse toResponse(ClienteEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "contrasena", ignore = true)
    @Mapping(target = "estado", defaultValue = "true")
    void replaceEntity(ClienteRequest request, @MappingTarget ClienteEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "identificacion", ignore = true)
    void updateEntity(ClienteUpdateRequest request, @MappingTarget ClienteEntity entity);
}
