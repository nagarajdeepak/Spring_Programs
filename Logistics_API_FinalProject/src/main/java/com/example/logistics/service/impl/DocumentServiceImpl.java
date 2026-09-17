package com.example.logistics.service.impl;

import com.example.logistics.dto.response.DocumentResponse;
import com.example.logistics.entity.Document;
import com.example.logistics.entity.Shipment;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.repository.DocumentRepository;
import com.example.logistics.repository.ShipmentRepository;
import com.example.logistics.service.DocumentService;
import com.example.logistics.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ShipmentRepository shipmentRepository;
    private final FileStorageService fileStorageService;

    @Override
    public DocumentResponse upload(
            Long shipmentId,
            MultipartFile file) {

        // Validate file
        validateFile(file);

        // Check shipment
        Shipment shipment =
                shipmentRepository.findById(shipmentId)
                    .orElseThrow(() ->
                        ResourceNotFoundException.of(
                            "Shipment",
                            shipmentId
                        )
                    );

        String storedFileName = null;

        try {

            /*
             * Store the physical file first.
             */
            storedFileName =
                    fileStorageService.store(file);

            /*
             * Create DB metadata.
             */
            Document document =
                    Document.builder()
                        .originalFileName(
                            sanitizeOriginalFileName(
                                file.getOriginalFilename()
                            )
                        )
                        .storedFileName(storedFileName)
                        .contentType(
                            file.getContentType()
                        )
                        .fileSize(file.getSize())
                        .shipment(shipment)
                        .build();

            Document saved =
                    documentRepository.save(document);

            log.info(
                "Uploaded document id={} ('{}') for shipment id={}",
                saved.getId(),
                saved.getOriginalFileName(),
                shipmentId
            );

            return toResponse(saved);

        } catch (RuntimeException ex) {

            /*
             * If DB save fails after the physical file
             * was successfully stored, remove the file.
             */
            if (storedFileName != null) {

                try {
                    fileStorageService.delete(
                        storedFileName
                    );
                } catch (Exception cleanupException) {

                    log.error(
                        "Failed to clean up stored file '{}'",
                        storedFileName,
                        cleanupException
                    );
                }
            }

            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getByShipment(
            Long shipmentId) {

        if (!shipmentRepository.existsById(shipmentId)) {

            throw ResourceNotFoundException.of(
                "Shipment",
                shipmentId
            );
        }

        return documentRepository
                .findByShipmentId(shipmentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponse getMetadata(Long id) {

        return toResponse(
            findEntity(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadPayload download(Long id) {

        Document document =
                findEntity(id);

        Resource resource =
                fileStorageService.loadAsResource(
                    document.getStoredFileName()
                );

        return new DownloadPayload(
            resource,
            document.getOriginalFileName(),
            document.getContentType()
        );
    }

    @Override
    public void delete(Long id) {

        Document document =
                findEntity(id);

        String storedFileName =
                document.getStoredFileName();

        /*
         * Delete physical file first.
         */
        fileStorageService.delete(
            storedFileName
        );

        /*
         * Then delete DB metadata.
         */
        documentRepository.delete(document);

        log.info(
            "Deleted document id={}",
            id
        );
    }

    private Document findEntity(Long id) {

        return documentRepository
                .findById(id)
                .orElseThrow(() ->
                    ResourceNotFoundException.of(
                        "Document",
                        id
                    )
                );
    }

    private DocumentResponse toResponse(
            Document document) {

        return DocumentResponse.builder()
                .id(document.getId())
                .originalFileName(
                    document.getOriginalFileName()
                )
                .contentType(
                    document.getContentType()
                )
                .fileSize(
                    document.getFileSize()
                )
                .shipmentId(
                    document.getShipment().getId()
                )
                .downloadUrl(
                    "/api/documents/"
                    + document.getId()
                    + "/download"
                )
                .createdAt(
                    document.getCreatedAt()
                )
                .build();
    }

    private void validateFile(
            MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                "File must not be empty"
            );
        }

        /*
         * 10 MB maximum.
         */
        long maxSize =
                10L * 1024L * 1024L;

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                "File size must not exceed 10 MB"
            );
        }
    }

    private String sanitizeOriginalFileName(
            String fileName) {

        if (fileName == null ||
            fileName.isBlank()) {

            return "file";
        }

        /*
         * Keep only the filename portion.
         */
        String cleaned =
                fileName
                    .replace("\\", "/");

        int lastSlash =
                cleaned.lastIndexOf('/');

        if (lastSlash >= 0) {
            cleaned =
                cleaned.substring(
                    lastSlash + 1
                );
        }

        /*
         * Remove CR/LF and quotes.
         */
        cleaned =
                cleaned
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace("\"", "");

        return cleaned.isBlank()
                ? "file"
                : cleaned;
    }
}