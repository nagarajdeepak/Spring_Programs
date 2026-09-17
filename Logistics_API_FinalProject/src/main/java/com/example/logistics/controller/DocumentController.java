package com.example.logistics.controller;

import com.example.logistics.dto.response.DocumentResponse;
import com.example.logistics.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(
    name = "Documents",
    description = "File attachments linked to shipments"
)
@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Upload a document for a shipment.
     *
     * Only ADMIN and OPERATOR can upload documents.
     */
    @Operation(summary = "Upload a document for a shipment")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PostMapping(
        value = "/api/shipments/{shipmentId}/documents",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentResponse> upload(
            @PathVariable Long shipmentId,
            @RequestParam("file") MultipartFile file) {

        DocumentResponse created =
                documentService.upload(shipmentId, file);

        return ResponseEntity
                .created(
                    URI.create(
                        "/api/documents/" + created.getId()
                    )
                )
                .body(created);
    }

    /**
     * List documents belonging to a shipment.
     *
     * Only ADMIN and OPERATOR can view document metadata.
     */
    @Operation(summary = "List documents attached to a shipment")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/api/shipments/{shipmentId}/documents")
    public ResponseEntity<List<DocumentResponse>> listForShipment(
            @PathVariable Long shipmentId) {

        return ResponseEntity.ok(
            documentService.getByShipment(shipmentId)
        );
    }

    /**
     * Get document metadata.
     *
     * Only ADMIN and OPERATOR can view document metadata.
     */
    @Operation(summary = "Get a document's metadata")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/api/documents/{id}")
    public ResponseEntity<DocumentResponse> getMetadata(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            documentService.getMetadata(id)
        );
    }

    /**
     * Download document content.
     *
     * Only ADMIN and OPERATOR can download files.
     */
    @Operation(summary = "Download a document's file content")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/api/documents/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long id) {

        DocumentService.DownloadPayload payload =
                documentService.download(id);

        String originalFileName =
                payload.originalFileName() == null
                    ? "file"
                    : payload.originalFileName();

        String encodedName =
                URLEncoder
                    .encode(
                        originalFileName,
                        StandardCharsets.UTF_8
                    )
                    .replace("+", "%20");

        MediaType mediaType;

        try {

            mediaType =
                payload.contentType() != null
                    ? MediaType.parseMediaType(
                        payload.contentType()
                    )
                    : MediaType.APPLICATION_OCTET_STREAM;

        } catch (Exception ex) {

            mediaType =
                MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\""
                        + sanitizeHeaderValue(originalFileName)
                        + "\"; filename*=UTF-8''"
                        + encodedName
                )
                .body(payload.resource());
    }

    /**
     * Delete a document.
     *
     * Only ADMIN and OPERATOR can delete documents.
     */
    @Operation(summary = "Delete a document")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @DeleteMapping("/api/documents/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        documentService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Prevent CR/LF characters from entering HTTP headers.
     */
    private String sanitizeHeaderValue(String value) {

        return value
                .replace("\r", "")
                .replace("\n", "")
                .replace("\"", "");
    }
}