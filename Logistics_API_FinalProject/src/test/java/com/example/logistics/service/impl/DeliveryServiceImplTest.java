package com.example.logistics.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.logistics.dto.request.DeliveryRequest;
import com.example.logistics.dto.response.DeliveryResponse;
import com.example.logistics.entity.Delivery;
import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.enums.DeliveryStatus;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.DeliveryMapper;
import com.example.logistics.repository.DeliveryRepository;
import com.example.logistics.repository.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Test
    void create_ValidRequest_ReturnsResponse() {
        DeliveryRequest request = new DeliveryRequest();
        request.setShipmentId(1L);
        request.setStatus(DeliveryStatus.PENDING);

        Shipment shipment = new Shipment();
        shipment.setId(1L);

        Delivery savedDelivery = new Delivery();
        DeliveryResponse expectedResponse = new DeliveryResponse();

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(deliveryRepository.findByShipmentId(1L)).thenReturn(Optional.empty());
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(savedDelivery);
        when(deliveryMapper.toResponse(savedDelivery)).thenReturn(expectedResponse);

        DeliveryResponse result = deliveryService.create(request);

        assertNotNull(result);
        verify(deliveryRepository).save(any(Delivery.class));
    }

    @Test
    void update_ValidIdAndRequest_UpdatesAndReturnsResponse() {
        Long id = 1L;
        DeliveryRequest request = new DeliveryRequest();
        request.setReceivedBy("John Doe");
        request.setStatus(DeliveryStatus.PENDING);

        Delivery existingDelivery = new Delivery();
        Delivery updatedDelivery = new Delivery();
        DeliveryResponse responseDto = new DeliveryResponse();

        when(deliveryRepository.findById(id)).thenReturn(Optional.of(existingDelivery));
        when(deliveryRepository.save(existingDelivery)).thenReturn(updatedDelivery);
        when(deliveryMapper.toResponse(updatedDelivery)).thenReturn(responseDto);

        DeliveryResponse result = deliveryService.update(id, request);

        assertNotNull(result);
        verify(deliveryRepository).save(existingDelivery);
    }

    @Test
    void getById_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(deliveryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deliveryService.getById(id));
    }
}