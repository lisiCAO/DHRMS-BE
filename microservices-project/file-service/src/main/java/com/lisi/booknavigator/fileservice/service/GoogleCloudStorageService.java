package com.lisi.booknavigator.fileservice.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.BlobId;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudStorageService {

    private final Storage storage;

    public GoogleCloudStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public URL generateSignedPublicUrl(String gcsUrl) {
        // 解析 gcsUrl，例如 "gs://bucket-name/object-name"
        String[] parts = gcsUrl.replace("gs://", "").split("/", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid GCS URL format.");
        }
        String bucketName = parts[0];
        String objectName = parts[1];

        // Optional: Remove any leading slashes from the object name to clean it up
        objectName = objectName.replaceAll("^/*", "");

        log.info("gsurl= {} ", gcsUrl);
        log.info("bucketName= {} ", bucketName);
        log.info("objectName= {} ", objectName);

        // 获取对应的blob信息
        BlobInfo blobInfo = storage.get(bucketName, objectName);

        // 生成签名的URL , 有效期为30分钟
        URL publicUrl = storage.signUrl(blobInfo, 30, TimeUnit.MINUTES, SignUrlOption.withV4Signature());
        log.info("publicUrl= {} ", publicUrl);
        return publicUrl;
    }

    public void deleteSingleFileByUrl(String filePath) throws IOException {
        log.info("Trying to delete file from Google Cloud Storage at {}", filePath);

        // get bucket name and object name from the file path
        String[] parts = filePath.replace("gs://", "").split("/", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid GCS URL format.");
        }
        String bucketName = parts[0];
        String objectName = parts[1];

        // Optional: Remove any leading slashes from the object name to clean it up
        objectName = objectName.replaceAll("^/*", "");

        try {
            //log.info("Beginning deletion...");
            // use storage to delete the file
            boolean deleted = storage.delete(BlobId.of(bucketName, objectName));
            //log.info("End deletion...");
            if (!deleted) {
                throw new IOException("Failed to delete file at " + filePath);
            }
            log.info("File deleted from Google Cloud Storage at {}", filePath);
        } catch (Exception e) {
            log.error("Failed to delete file from GCS: {}", e.getMessage(), e);
            throw new IOException("Failed to delete file", e);
        }
    }
}
