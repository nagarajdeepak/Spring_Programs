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
public class DocumentResponse {

    private Long id;
    private String originalFileName;
    private String contentType;
    private long fileSize;
    private Long shipmentId;
    private String downloadUrl;
    private LocalDateTime createdAt;
}