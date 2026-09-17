package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.InventoryRequest;
import com.example.logistics.dto.response.InventoryResponse;

public interface InventoryService {
    InventoryResponse create(InventoryRequest request);
    InventoryResponse getById(Long id);
    List<InventoryResponse> getAll();
    InventoryResponse update(Long id, InventoryRequest request);
    void delete(Long id);
}