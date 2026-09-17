package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.WarehouseRequest;
import com.example.logistics.dto.response.WarehouseResponse;

public interface WarehouseService {
    WarehouseResponse create(WarehouseRequest request);
    WarehouseResponse getById(Long id);
    List<WarehouseResponse> getAll();
    WarehouseResponse update(Long id, WarehouseRequest request);
    void delete(Long id);
}
