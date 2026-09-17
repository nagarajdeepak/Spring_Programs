package com.example.logistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.example.logistics.dto.response.DeliveryResponse;
import com.example.logistics.entity.Delivery;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {

    @Mapping(target = "shipmentId", source = "shipment.id")
    DeliveryResponse toResponse(Delivery delivery);
}