package com.example.logistics.service.impl;

import com.example.logistics.dto.request.DeliveryRequest;
import com.example.logistics.dto.response.DeliveryResponse;
import com.example.logistics.entity.Delivery;
import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.enums.DeliveryStatus;
import com.example.logistics.entity.enums.ShipmentStatus;
import com.example.logistics.exception.DuplicateResourceException;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.DeliveryMapper;
import com.example.logistics.repository.DeliveryRepository;
import com.example.logistics.repository.ShipmentRepository;
import com.example.logistics.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ShipmentRepository shipmentRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryResponse create(DeliveryRequest request) {
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> ResourceNotFoundException.of("Shipment", request.getShipmentId()));

        if (deliveryRepository.findByShipmentId(shipment.getId()).isPresent()) {
            throw new DuplicateResourceException("Shipment id " + shipment.getId() + " already has a delivery record");
        }

        Delivery delivery = Delivery.builder()
                .receivedBy(request.getReceivedBy())
                .status(request.getStatus())
                .signatureNote(request.getSignatureNote())
                .deliveredAt(request.getStatus() == DeliveryStatus.SUCCESSFUL ? LocalDateTime.now() : null)
                .shipment(shipment)
                .build();

        Delivery saved = deliveryRepository.save(delivery);

        // Recording a successful delivery also closes out the parent shipment.
        if (saved.getStatus() == DeliveryStatus.SUCCESSFUL) {
            shipment.setStatus(ShipmentStatus.DELIVERED);
            shipmentRepository.save(shipment);
        }

        log.info("Created delivery id={} for shipment id={}", saved.getId(), shipment.getId());
        return deliveryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getById(Long id) {
        return deliveryMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAll() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toResponse)
                .toList();
    }

    @Override
    public DeliveryResponse update(Long id, DeliveryRequest request) {
        Delivery existing = findEntity(id);

        existing.setReceivedBy(request.getReceivedBy());
        existing.setStatus(request.getStatus());
        existing.setSignatureNote(request.getSignatureNote());
        if (request.getStatus() == DeliveryStatus.SUCCESSFUL && existing.getDeliveredAt() == null) {
            existing.setDeliveredAt(LocalDateTime.now());
            existing.getShipment().setStatus(ShipmentStatus.DELIVERED);
            shipmentRepository.save(existing.getShipment());
        }

        Delivery updated = deliveryRepository.save(existing);
        log.info("Updated delivery id={}", id);
        return deliveryMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Delivery existing = findEntity(id);
        deliveryRepository.delete(existing);
        log.info("Deleted delivery id={}", id);
    }

    private Delivery findEntity(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Delivery", id));
    }
}
