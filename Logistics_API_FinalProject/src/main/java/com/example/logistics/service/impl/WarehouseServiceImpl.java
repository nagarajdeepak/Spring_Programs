package com.example.logistics.service.impl;

import com.example.logistics.dto.request.WarehouseRequest;
import com.example.logistics.dto.response.WarehouseResponse;
import com.example.logistics.entity.Warehouse;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.WarehouseMapper;
import com.example.logistics.repository.WarehouseRepository;
import com.example.logistics.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponse create(WarehouseRequest request) {
        Warehouse saved = warehouseRepository.save(warehouseMapper.toEntity(request));
        log.info("Created warehouse id={}", saved.getId());
        return warehouseMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getById(Long id) {
        return warehouseMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream()
                .map(warehouseMapper::toResponse)
                .toList();
    }

    @Override
    public WarehouseResponse update(Long id, WarehouseRequest request) {
        Warehouse existing = findEntity(id);
        existing.setName(request.getName());
        existing.setLocation(request.getLocation());
        existing.setCapacity(request.getCapacity());
        Warehouse updated = warehouseRepository.save(existing);
        log.info("Updated warehouse id={}", id);
        return warehouseMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Warehouse existing = findEntity(id);
        warehouseRepository.delete(existing);
        log.info("Deleted warehouse id={}", id);
    }

    private Warehouse findEntity(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Warehouse", id));
    }
}
