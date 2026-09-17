package com.example.logistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.example.logistics.dto.request.CustomerRequest;
import com.example.logistics.dto.response.CustomerResponse;
import com.example.logistics.entity.Customer;

/**
 * MapStruct generates the implementation of this interface at compile time
 * (see mapstruct-processor in pom.xml) - no reflection, no hand-written
 * boilerplate for entity <-> DTO conversion.
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    Customer toEntity(CustomerRequest request);

    CustomerResponse toResponse(Customer customer);
}