package com.example.logistics.dto.request;

import com.example.logistics.entity.enums.DeliveryStatus;

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
public class DeliveryRequest {

    private String receivedBy;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    private String signatureNote;

    @NotNull(message = "Shipment id is required")
    private Long shipmentId;
}