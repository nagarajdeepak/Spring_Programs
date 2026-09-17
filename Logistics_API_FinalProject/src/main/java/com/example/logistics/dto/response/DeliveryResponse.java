package com.example.logistics.dto.response;

import java.time.LocalDateTime;

import com.example.logistics.entity.enums.DeliveryStatus;

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
public class DeliveryResponse {

    private Long id;
    private String receivedBy;
    private DeliveryStatus status;
    private LocalDateTime deliveredAt;
    private String signatureNote;
    private Long shipmentId;
}
