package com.lisi.booknavigator.fileservice.service;

import com.lisi.booknavigator.fileservice.exception.FileIOException;
import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.repository.FileRepository;
import com.lisi.booknavigator.fileservice.dto.FileRequest;
import com.lisi.booknavigator.fileservice.dto.StorageResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final StorageService storageService;
//    private final KafkaTemplate<String, FileSavedEvent> kafkaTemplate;
    private final Tracer tracer;
    public String saveFile(FileRequest fileRequest) throws IOException {

        Span storageSpan = tracer.nextSpan().name("Storage Service Call");
        try (Tracer.SpanInScope ws = tracer.withSpan(storageSpan.start())) {
            log.info("Calling Google Storage for file upload");

            StorageResponse storageResponse = storageService.uploadFile(fileRequest.getFile(), fileRequest.getFileType());

            File file = new File(null, storageResponse.getUrl(), storageResponse.getFileType(), storageResponse.getUploadDate(),
                    fileRequest.getAssociatedEntityId(), fileRequest.getAssociatedEntityType(), fileRequest.getUserId());

            fileRepository.save(file);
//
//            kafkaTemplate.send("file-events", new FileSavedEvent(file.getUrl()));
            log.info("File uploaded and saved with ID: {}", file.getId());

            return "File Saved Successfully";

        } catch (IOException e) {
            log.error("Failed to upload file to GCS: {}", e.getMessage());
            throw new FileIOException("Failed to upload file", e);
        } finally {
            storageSpan.end();
        }
    }

    public List<File> listFiles(Integer associatedEntityId, String associatedEntityType) {
        // Here we might need to add logic to filter based on parameters
        return fileRepository.findAll();
    }

    public File getFileById(Integer fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}