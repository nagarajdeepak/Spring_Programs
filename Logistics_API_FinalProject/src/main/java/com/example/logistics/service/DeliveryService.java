package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.DeliveryRequest;
import com.example.logistics.dto.response.DeliveryResponse;

public interface DeliveryService {
    DeliveryResponse create(DeliveryRequest request);
    DeliveryResponse getById(Long id);
    List<DeliveryResponse> getAll();
    DeliveryResponse update(Long id, DeliveryRequest request);
    void delete(Long id);
}
