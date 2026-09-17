package com.example.logistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingRequest {

    @NotBlank(message = "Current location is required")
    private String currentLocation;

    @NotBlank(message = "Status description is required")
    private String statusDescription;

    @NotNull(message = "Shipment id is required")
    private Long shipmentId;
}
