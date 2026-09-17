package com.example.logistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.file-storage")
public class FileStorageProperties {

    /**
     * Directory where uploaded files are stored.
     */
    private String uploadDir = "uploads";

    /**
     * Maximum allowed file size in bytes.
     * Default: 10 MB.
     */
    private long maxFileSize = 10 * 1024 * 1024;

    /**
     * Allowed MIME/content types.
     */
    private String[] allowedContentTypes = {
        "application/pdf",
        "image/png",
        "image/jpeg",
        "image/jpg",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };

    /**
     * Allowed file extensions.
     */
    private String[] allowedExtensions = {
        ".pdf",
        ".png",
        ".jpg",
        ".jpeg",
        ".doc",
        ".docx"
    };
}