package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.ShipmentRequest;
import com.example.logistics.dto.response.ShipmentResponse;

public interface ShipmentService {
    ShipmentResponse create(ShipmentRequest request);
    ShipmentResponse getById(Long id);
    List<ShipmentResponse> getAll();
    ShipmentResponse update(Long id, ShipmentRequest request);
    void delete(Long id);
}
