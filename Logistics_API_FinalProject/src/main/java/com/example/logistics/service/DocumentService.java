package com.example.logistics.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.logistics.dto.response.DocumentResponse;

public interface DocumentService {

    DocumentResponse upload(
            Long shipmentId,
            MultipartFile file
    );

    List<DocumentResponse> getByShipment(
            Long shipmentId
    );

    DocumentResponse getMetadata(
            Long id
    );

    /**
     * Contains the file resource and metadata
     * required for the HTTP download response.
     */
    record DownloadPayload(
            Resource resource,
            String originalFileName,
            String contentType
    ) {}

    DownloadPayload download(
            Long id
    );

    void delete(
            Long id
    );
}