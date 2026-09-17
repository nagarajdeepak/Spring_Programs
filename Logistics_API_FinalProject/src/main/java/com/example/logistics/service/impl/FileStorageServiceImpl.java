package com.example.logistics.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.logistics.config.FileStorageProperties;
import com.example.logistics.exception.FileStorageException;
import com.example.logistics.service.FileStorageService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties properties;

    private Path storageLocation;

    @PostConstruct
    public void init() {

        storageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(storageLocation);

            log.info(
                "File storage directory ready at {}",
                storageLocation
            );

        } catch (IOException e) {

            throw new FileStorageException(
                "Could not create upload directory: " + storageLocation,
                e
            );
        }
    }

    @Override
    public String store(MultipartFile file) {

        // 1. Basic validation
        if (file == null || file.isEmpty()) {
            throw new FileStorageException(
                "Cannot store an empty file"
            );
        }

        // 2. File size validation
        long maxFileSize = properties.getMaxFileSize();

        if (maxFileSize > 0 && file.getSize() > maxFileSize) {
            throw new FileStorageException(
                "File size exceeds the maximum allowed size of "
                + maxFileSize + " bytes"
            );
        }

        // 3. Clean original filename
        String originalFileName = StringUtils.cleanPath(
            file.getOriginalFilename() == null
                ? "unnamed"
                : file.getOriginalFilename()
        );

        // 4. Prevent path traversal
        if (originalFileName.contains("..")) {
            throw new FileStorageException(
                "Filename contains an invalid path sequence: "
                + originalFileName
            );
        }

        // 5. Validate content type
        validateContentType(file.getContentType());

        // 6. Get extension
        String extension = "";

        int dotIndex = originalFileName.lastIndexOf('.');

        if (dotIndex >= 0) {
            extension = originalFileName.substring(dotIndex)
                    .toLowerCase();
        }

        // 7. Validate extension
        validateExtension(extension);

        // 8. Generate safe unique filename
        String storedFileName =
            UUID.randomUUID() + extension;

        try {

            Path targetPath = resolveSafePath(storedFileName);

            // 9. Store file
            Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            );

            log.info(
                "Stored file '{}' as '{}'",
                originalFileName,
                storedFileName
            );

            return storedFileName;

        } catch (IOException e) {

            throw new FileStorageException(
                "Failed to store file " + originalFileName,
                e
            );
        }
    }

    @Override
    public Resource loadAsResource(String storedFileName) {

        try {

            Path filePath = resolveSafePath(storedFileName);

            Resource resource =
                new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {

                throw new FileStorageException(
                    "File not found or not readable: "
                    + storedFileName
                );
            }

            return resource;

        } catch (MalformedURLException e) {

            throw new FileStorageException(
                "Could not read file: " + storedFileName,
                e
            );
        }
    }

    @Override
    public void delete(String storedFileName) {

        try {

            Path filePath =
                resolveSafePath(storedFileName);

            Files.deleteIfExists(filePath);

            log.info(
                "Deleted stored file '{}'",
                storedFileName
            );

        } catch (IOException e) {

            log.warn(
                "Could not delete stored file {}: {}",
                storedFileName,
                e.getMessage()
            );
        }
    }

    /**
     * Resolves a filename safely inside the storage directory.
     */
    private Path resolveSafePath(String fileName) {

        if (!StringUtils.hasText(fileName)) {

            throw new FileStorageException(
                "Stored filename cannot be empty"
            );
        }

        Path filePath =
            storageLocation.resolve(fileName).normalize();

        if (!filePath.getParent().equals(storageLocation)) {

            throw new FileStorageException(
                "Invalid file path: " + fileName
            );
        }

        return filePath;
    }

    /**
     * Validates the uploaded file MIME type.
     */
    private void validateContentType(String contentType) {

        String[] allowed =
            properties.getAllowedContentTypes();

        if (allowed == null || allowed.length == 0) {
            return;
        }

        if (contentType == null ||
            Arrays.stream(allowed)
                .noneMatch(contentType::equalsIgnoreCase)) {

            throw new FileStorageException(
                "Unsupported file type: " + contentType
            );
        }
    }

    /**
     * Validates the uploaded file extension.
     */
    private void validateExtension(String extension) {

        String[] allowed =
            properties.getAllowedExtensions();

        if (allowed == null || allowed.length == 0) {
            return;
        }

        if (Arrays.stream(allowed)
                .noneMatch(extension::equalsIgnoreCase)) {

            throw new FileStorageException(
                "Unsupported file extension: "
                + extension
            );
        }
    }
}