package com.example.logistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.example.logistics.dto.response.TrackingResponse;
import com.example.logistics.entity.Tracking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrackingMapper {

    @Mapping(target = "shipmentId", source = "shipment.id")
    TrackingResponse toResponse(Tracking tracking);
}