package com.example.logistics.controller;

import com.example.logistics.dto.request.TrackingRequest;
import com.example.logistics.dto.response.TrackingResponse;
import com.example.logistics.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Tracking", description = "Tracking updates provide real-time location and status of shipments")
@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @Operation(summary = "Add a tracking update")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PostMapping
    public ResponseEntity<TrackingResponse> create(@Valid @RequestBody TrackingRequest request) {
        TrackingResponse created = trackingService.create(request);
        return ResponseEntity.created(URI.create("/api/tracking/" + created.getId())).body(created);
    }

    @Operation(summary = "List all tracking entries")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping
    public ResponseEntity<List<TrackingResponse>> getAll() {
        return ResponseEntity.ok(trackingService.getAll());
    }

    @Operation(summary = "Get a tracking entry by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TrackingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trackingService.getById(id));
    }

    @Operation(summary = "Update a tracking entry")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TrackingResponse> update(@PathVariable Long id, @Valid @RequestBody TrackingRequest request) {
        return ResponseEntity.ok(trackingService.update(id, request));
    }

    @Operation(summary = "Delete a tracking entry")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trackingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
