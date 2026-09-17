package com.example.logistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.example.logistics.dto.request.WarehouseRequest;
import com.example.logistics.dto.response.WarehouseResponse;
import com.example.logistics.entity.Warehouse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {

    Warehouse toEntity(WarehouseRequest request);

    @Mapping(target = "itemCount", expression = "java(warehouse.getInventoryItems() == null ? 0 : warehouse.getInventoryItems().size())")
    WarehouseResponse toResponse(Warehouse warehouse);
}