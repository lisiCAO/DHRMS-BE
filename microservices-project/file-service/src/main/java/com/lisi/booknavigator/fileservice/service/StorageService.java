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
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final ResourceLoader resourceLoader;

    public StorageResponse uploadFile(MultipartFile file, String fileType, String entityType, String entityId) throws IOException {
        String blobPath = "gs://" + determineBucketName(fileType) + "/" + entityType + "/" +  entityId + "/" + file.getOriginalFilename();
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

    public String determineBucketName(String fileType) {
        String bucketBasePath = "dhrms-realestate-image";
        return switch (fileType.toLowerCase()) {
            case "image" -> bucketBasePath + "/images";
            case "document" -> bucketBasePath + "/documents";
            case "video" -> bucketBasePath + "/videos";
            default -> bucketBasePath + "/others";
        };
    }

//    public void deleteSingleFile(String filePath) throws IOException {
//        log.info("try to delete file from Google Cloud Storage at {}", filePath);
//        WritableResource resource = (WritableResource) resourceLoader.getResource(filePath);
//        try {
//            log.info("begin delete...");
//            boolean deleted = resource.getFile().delete();
//            log.info("end delete...");
//            if (!deleted) {
//                throw new IOException("Failed to delete file at " + filePath);
//            }
//            log.info("File deleted from Google Cloud Storage at {}", filePath);
//        } catch (IOException e) {
//            log.error("Failed to delete file from GCS: {}", e.getMessage(), e);
//            throw e;
//        }
//    }

    /**
     * Extracts the object name from a Google Cloud Storage path.
     *
     * @param gcsPath The full GCS path, e.g., "gs://xxxxx-xxxxxxxx-image/images/entitytype/entityid/Alice.jpeg"
     * @return The object name with leading slash, e.g., "Alice.jpeg", or null if not found
     */
    public String getObjectName(String gcsPath) {
        // get the index of the last slash after "gs://"
        int lastIndex = gcsPath.lastIndexOf('/');
        // if find the last slash and it is not the last character
        if (lastIndex != -1 && lastIndex != gcsPath.length() - 1) {
            return gcsPath.substring(lastIndex + 1);
        }
        return null;
    }


}
