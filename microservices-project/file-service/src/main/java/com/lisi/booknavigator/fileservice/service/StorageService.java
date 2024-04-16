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
        String blobPath = "gs://" + determineBucketName(fileType) + "/" + file.getOriginalFilename();
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
        String bucketBasePath = "dhrms-realestate-image";
        return switch (fileType.toLowerCase()) {
            case "image" -> bucketBasePath + "/images";
            case "document" -> bucketBasePath + "/documents";
            case "video" -> bucketBasePath + "/videos";
            default -> bucketBasePath + "/others";
        };
    }
}
