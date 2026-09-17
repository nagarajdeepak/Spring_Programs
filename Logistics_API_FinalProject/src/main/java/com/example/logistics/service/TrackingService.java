package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.TrackingRequest;
import com.example.logistics.dto.response.TrackingResponse;

public interface TrackingService {
    TrackingResponse create(TrackingRequest request);
    TrackingResponse getById(Long id);
    List<TrackingResponse> getAll();
    TrackingResponse update(Long id, TrackingRequest request);
    void delete(Long id);
}
