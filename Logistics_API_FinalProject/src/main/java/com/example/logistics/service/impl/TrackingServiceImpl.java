package com.example.logistics.service.impl;

import com.example.logistics.dto.request.TrackingRequest;
import com.example.logistics.dto.response.TrackingResponse;
import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.Tracking;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.TrackingMapper;
import com.example.logistics.repository.ShipmentRepository;
import com.example.logistics.repository.TrackingRepository;
import com.example.logistics.service.TrackingService;
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
public class TrackingServiceImpl implements TrackingService {

    private final TrackingRepository trackingRepository;
    private final ShipmentRepository shipmentRepository;
    private final TrackingMapper trackingMapper;

    @Override
    public TrackingResponse create(TrackingRequest request) {
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> ResourceNotFoundException.of("Shipment", request.getShipmentId()));

        Tracking tracking = Tracking.builder()
                .currentLocation(request.getCurrentLocation())
                .statusDescription(request.getStatusDescription())
                .recordedAt(LocalDateTime.now())
                .shipment(shipment)
                .build();

        Tracking saved = trackingRepository.save(tracking);
        log.info("Created tracking update id={} for shipment id={}", saved.getId(), shipment.getId());
        return trackingMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackingResponse getById(Long id) {
        return trackingMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackingResponse> getAll() {
        return trackingRepository.findAll().stream()
                .map(trackingMapper::toResponse)
                .toList();
    }

    @Override
    public TrackingResponse update(Long id, TrackingRequest request) {
        Tracking existing = findEntity(id);

        if (!existing.getShipment().getId().equals(request.getShipmentId())) {
            Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Shipment", request.getShipmentId()));
            existing.setShipment(shipment);
        }

        existing.setCurrentLocation(request.getCurrentLocation());
        existing.setStatusDescription(request.getStatusDescription());

        Tracking updated = trackingRepository.save(existing);
        log.info("Updated tracking entry id={}", id);
        return trackingMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Tracking existing = findEntity(id);
        trackingRepository.delete(existing);
        log.info("Deleted tracking entry id={}", id);
    }

    private Tracking findEntity(Long id) {
        return trackingRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Tracking", id));
    }
}
