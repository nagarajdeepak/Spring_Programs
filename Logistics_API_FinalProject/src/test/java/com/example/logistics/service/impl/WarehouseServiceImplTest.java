package com.example.logistics.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.logistics.dto.response.WarehouseResponse;
import com.example.logistics.entity.Warehouse;
import com.example.logistics.mapper.WarehouseMapper;
import com.example.logistics.repository.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Test
    void getWarehouseById_Success() {
        Long id = 5L;
        Warehouse warehouse = new Warehouse();
        WarehouseResponse dto = new WarehouseResponse();

        when(warehouseRepository.findById(id)).thenReturn(Optional.of(warehouse));
        when(warehouseMapper.toResponse(warehouse)).thenReturn(dto);

        WarehouseResponse result = warehouseService.getById(id);

        assertNotNull(result);
    }
}