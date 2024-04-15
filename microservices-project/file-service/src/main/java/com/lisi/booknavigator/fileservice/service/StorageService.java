package com.lisi.booknavigator.fileservice.service;

import com.lisi.booknavigator.fileservice.dto.StorageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final ResourceLoader resourceLoader;

    public StorageResponse uploadFile(MultipartFile file, String fileType) throws IOException {
        String bucketName = determineBucketName(fileType);
        String blobPath = "gs://" + bucketName + "/" + file.getOriginalFilename();
        WritableResource resource = (WritableResource) resourceLoader.getResource(blobPath);

        try (OutputStream os = resource.getOutputStream()) {
            os.write(file.getBytes());
            log.info("File uploaded to Google Cloud Storage at {}", blobPath);
            return new StorageResponse(blobPath, fileType, LocalDateTime.now(), file.getSize());
        } catch (IOException e) {
            log.error("Failed to upload file to GCS: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String determineBucketName(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "image" -> "image-bucket";
            case "document" -> "document-bucket";
            case "video" -> "video-bucket";
            default -> "default-bucket";
        };
    }
}
