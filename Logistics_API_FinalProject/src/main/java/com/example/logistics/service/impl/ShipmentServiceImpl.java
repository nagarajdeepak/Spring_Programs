package com.example.logistics.service.impl;

import com.example.logistics.dto.request.ShipmentRequest;
import com.example.logistics.dto.response.ShipmentResponse;
import com.example.logistics.entity.Customer;
import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.enums.ShipmentStatus;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.ShipmentMapper;
import com.example.logistics.repository.CustomerRepository;
import com.example.logistics.repository.ShipmentRepository;
import com.example.logistics.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CustomerRepository customerRepository;
    private final ShipmentMapper shipmentMapper;

    @Override
    public ShipmentResponse create(ShipmentRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> ResourceNotFoundException.of("Customer", request.getCustomerId()));

        Shipment shipment = Shipment.builder()
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(request.getStatus() == null ? ShipmentStatus.CREATED : request.getStatus())
                .weightKG(request.getWeightKg())
                .customer(customer)
                .build();

        Shipment saved = shipmentRepository.save(shipment);
        log.info("Created shipment id={} for customer id={}", saved.getId(), customer.getId());
        return shipmentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getById(Long id) {
        return shipmentMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> getAll() {
        return shipmentRepository.findAll().stream()
                .map(shipmentMapper::toResponse)
                .toList();
    }

    @Override
    public ShipmentResponse update(Long id, ShipmentRequest request) {
        Shipment existing = findEntity(id);

        if (!existing.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Customer", request.getCustomerId()));
            existing.setCustomer(customer);
        }

        existing.setOrigin(request.getOrigin());
        existing.setDestination(request.getDestination());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        existing.setWeightKG(request.getWeightKg());

        Shipment updated = shipmentRepository.save(existing);
        log.info("Updated shipment id={}", id);
        return shipmentMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Shipment existing = findEntity(id);
        shipmentRepository.delete(existing);
        log.info("Deleted shipment id={}", id);
    }

    private Shipment findEntity(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Shipment", id));
    }
}
