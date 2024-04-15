package com.lisi.booknavigator.fileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Autowired
    private ResourceLoader resourceLoader;

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String blobPath = "gs://" + bucketName + "/" + file.getOriginalFilename();
        WritableResource resource = (WritableResource) resourceLoader.getResource(blobPath);
        try (OutputStream os = resource.getOutputStream()) {
            os.write(file.getBytes());
        }
        return blobPath;
    }
}
