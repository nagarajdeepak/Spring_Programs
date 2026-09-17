package com.example.logistics.service.impl;

import com.example.logistics.dto.request.InventoryRequest;
import com.example.logistics.dto.response.InventoryResponse;
import com.example.logistics.entity.InventoryItem;
import com.example.logistics.entity.Warehouse;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.InventoryMapper;
import com.example.logistics.repository.InventoryRepository;
import com.example.logistics.repository.WarehouseRepository;
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
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void create_ValidRequest_ReturnsResponse() {
        InventoryRequest request = new InventoryRequest();
        request.setWarehouseId(1L);
        request.setItemName("Widget");
        request.setQuantity(50);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        InventoryItem savedItem = new InventoryItem();
        InventoryResponse expectedResponse = new InventoryResponse();

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(savedItem);
        when(inventoryMapper.toResponse(savedItem)).thenReturn(expectedResponse);

        InventoryResponse actualResponse = inventoryService.create(request);

        assertNotNull(actualResponse);
        verify(warehouseRepository).findById(1L);
        verify(inventoryRepository).save(any(InventoryItem.class));
    }

    @Test
    void getById_ValidId_ReturnsResponse() {
        Long id = 1L;
        InventoryItem item = new InventoryItem();
        InventoryResponse expectedResponse = new InventoryResponse();

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(item));
        when(inventoryMapper.toResponse(item)).thenReturn(expectedResponse);

        InventoryResponse actualResponse = inventoryService.getById(id);

        assertNotNull(actualResponse);
        verify(inventoryRepository).findById(id);
    }

    @Test
    void getById_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getById(id));
    }
}