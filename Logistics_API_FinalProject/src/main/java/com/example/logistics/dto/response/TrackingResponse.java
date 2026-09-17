package com.example.logistics.dto.response;

import java.time.LocalDateTime;

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
public class TrackingResponse {

    private Long id;
    private String currentLocation;
    private String statusDescription;
    private LocalDateTime recordedAt;
    private Long shipmentId;
}
