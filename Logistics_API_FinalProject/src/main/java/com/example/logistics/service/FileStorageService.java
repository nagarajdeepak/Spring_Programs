package com.example.logistics.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstracts physical file storage from the rest of the app. The default
 * implementation writes to local disk; swap in an S3/GCS-backed implementation
 * later without touching DocumentService or the controller.
 */

public interface FileStorageService {

    /** Persists the file's bytes and returns the unique name it was stored under. */
    String store(MultipartFile file);

    /** Loads a previously stored file as a Spring Resource, ready to stream back to a client. */
    Resource loadAsResource(String storedFileName);

    /** Removes a previously stored file. Safe to call even if the file is already gone. */
    void delete(String storedFileName);
}