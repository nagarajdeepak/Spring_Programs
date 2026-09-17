package com.example.logistics.service.impl;

import com.example.logistics.dto.request.InventoryRequest;
import com.example.logistics.dto.response.InventoryResponse;
import com.example.logistics.entity.InventoryItem;
import com.example.logistics.entity.Warehouse;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.InventoryMapper;
import com.example.logistics.repository.InventoryRepository;
import com.example.logistics.repository.WarehouseRepository;
import com.example.logistics.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse create(InventoryRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> ResourceNotFoundException.of("Warehouse", request.getWarehouseId()));

        InventoryItem item = InventoryItem.builder()
                .itemName(request.getItemName())
                .sku(request.getSku())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .warehouse(warehouse)
                .build();

        InventoryItem saved = inventoryRepository.save(item);
        log.info("Created inventory item id={} in warehouse id={}", saved.getId(), warehouse.getId());
        return inventoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getById(Long id) {
        return inventoryMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAll() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    @Override
    public InventoryResponse update(Long id, InventoryRequest request) {
        InventoryItem existing = findEntity(id);

        if (!existing.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Warehouse", request.getWarehouseId()));
            existing.setWarehouse(warehouse);
        }

        existing.setItemName(request.getItemName());
        existing.setSku(request.getSku());
        existing.setQuantity(request.getQuantity());
        existing.setUnitPrice(request.getUnitPrice());

        InventoryItem updated = inventoryRepository.save(existing);
        log.info("Updated inventory item id={}", id);
        return inventoryMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        InventoryItem existing = findEntity(id);
        inventoryRepository.delete(existing);
        log.info("Deleted inventory item id={}", id);
    }

    private InventoryItem findEntity(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("InventoryItem", id));
    }
}
