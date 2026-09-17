package com.example.logistics.service.impl;

import com.example.logistics.dto.request.ShipmentRequest;
import com.example.logistics.dto.response.ShipmentResponse;
import com.example.logistics.entity.Customer;
import com.example.logistics.entity.Shipment;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.ShipmentMapper;
import com.example.logistics.repository.CustomerRepository;
import com.example.logistics.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShipmentMapper shipmentMapper;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Test
    void create_ValidRequest_ReturnsResponse() {
        ShipmentRequest request = new ShipmentRequest();
        request.setCustomerId(1L);
        request.setOrigin("NYC");
        request.setDestination("LAX");

        Customer customer = new Customer();
        customer.setId(1L);

        Shipment savedShipment = new Shipment();
        ShipmentResponse expectedResponse = new ShipmentResponse();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);
        when(shipmentMapper.toResponse(savedShipment)).thenReturn(expectedResponse);

        ShipmentResponse actualResponse = shipmentService.create(request);

        assertNotNull(actualResponse);
        verify(customerRepository).findById(1L);
        verify(shipmentRepository).save(any(Shipment.class));
        verify(shipmentMapper).toResponse(savedShipment);
    }

    @Test
    void getById_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(shipmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> shipmentService.getById(id));
        verify(shipmentRepository).findById(id);
    }
}