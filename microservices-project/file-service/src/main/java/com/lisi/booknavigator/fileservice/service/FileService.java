package com.lisi.booknavigator.fileservice.service;

import com.lisi.booknavigator.fileservice.exception.FileIOException;
import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.model.GCSUrl;
import com.lisi.booknavigator.fileservice.repository.FileRepository;
import com.lisi.booknavigator.fileservice.dto.FileRequest;
import com.lisi.booknavigator.fileservice.dto.StorageResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final StorageService storageService;
    private final GoogleCloudStorageService googleCloudStorageService;

//    private final KafkaTemplate<String, FileSavedEvent> kafkaTemplate;
    private final Tracer tracer;

    @Transactional
    public String saveFile(FileRequest fileRequest) throws IOException {

        Span storageSpan = tracer.nextSpan().name("Storage Service Call");
        try (Tracer.SpanInScope ws = tracer.withSpan(storageSpan.start())) {
            log.info("Calling Google Storage for file upload");

            StorageResponse storageResponse = storageService.uploadFile(fileRequest.getFile(), fileRequest.getFileType(), fileRequest.getAssociatedEntityType(), fileRequest.getAssociatedEntityId());

            if (storageResponse == null) {
                log.error("Failed to upload file to GCS");
                throw new FileIOException("Failed to upload file to GCS");
            }
            else {
                //check if the file exists in the database
                Optional<File> optionalFile = fileRepository.findByUrl(storageResponse.getUrl());
                if (optionalFile.isPresent()) {
                    log.info("file url = {} exists in the database", storageResponse.getUrl());

                    File file = optionalFile.get();

                    //update file record
                    file.setFileType(storageResponse.getFileType());
                    file.setUploadDate(storageResponse.getUploadDate());
                    file.setAssociatedEntityId(fileRequest.getAssociatedEntityId());
                    file.setAssociatedEntityType(fileRequest.getAssociatedEntityType());
                    file.setUserId(fileRequest.getUserId());

                    // update file record
                    fileRepository.save(file);
                    log.info("update the database with ID: {}", file.getId());

                    return "File already exists in the database, updated the record with ID: " + file.getId();

                } else { //url doesn't exist in the database
                    log.info("file url = {} doesn't exist in the database", storageResponse.getUrl());

                    File file = new File(null, storageResponse.getUrl(), storageResponse.getFileType(), storageResponse.getUploadDate(),
                            fileRequest.getAssociatedEntityId(), fileRequest.getAssociatedEntityType(), fileRequest.getUserId());
                    fileRepository.save(file);
//
//              kafkaTemplate.send("file-events", new FileSavedEvent(file.getUrl()));
                    log.info("create new record in the database with ID: {}", file.getId());

                    return "File uploaded successfully create new record with ID: " + file.getId();
                }
            }
        } catch (IOException e) {
            log.error("Failed to upload file to GCS: {}", e.getMessage());
            throw new FileIOException("Failed to upload file", e);
        } finally {
            storageSpan.end();
        }
    }

    // list all files in the database by the associatedEntityId and associatedEntityType
    public List<File> listFiles(String associatedEntityId, String associatedEntityType) {
        Sort sort = Sort.by(Sort.Direction.DESC, "uploadDate");

        if (associatedEntityId != null && associatedEntityType != null) {
            return fileRepository.findByAssociatedEntityIdAndAssociatedEntityType(associatedEntityId, associatedEntityType, sort);
        } else if (associatedEntityId != null) {
            return fileRepository.findByAssociatedEntityId(associatedEntityId, sort);
        } else if (associatedEntityType != null) {
            return fileRepository.findByAssociatedEntityType(associatedEntityType, sort);
        } else {
            return fileRepository.findAll(sort); // find all files in the database sorted by uploadDate
        }
    }

    public File getFileById(Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }

    public List<File> findByUrlContaining(String fileName) {
        return fileRepository.findByUrlContaining(fileName);
    }

    public void deleteSingleFileById(Long fileId) throws IOException {
        Span deleteSpan = tracer.nextSpan().name("Delete File Service Call");
        try (Tracer.SpanInScope ws = tracer.withSpan(deleteSpan.start())) {
            log.info("Attempting to delete file with ID {} from GCS", fileId);

            File file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + fileId));
            //check if the file exists in the database
            if (file == null) {
                log.error("File with ID {} not found", fileId);
                throw new IllegalArgumentException("File not found");
            }else {
                log.info("File with ID {} found, url {}", fileId, file.getUrl());
            }

            googleCloudStorageService.deleteSingleFileByUrl(file.getUrl());

            fileRepository.delete(file);

            log.info("File with ID {} has been successfully deleted", fileId);
        } catch (IOException e) {
            log.error("Failed to delete file from storage: {}", e.getMessage());
            throw new IOException("Failed to delete file", e);
        } catch (IllegalArgumentException e) {
            log.error("Failed to find file with ID {}: {}", fileId, e.getMessage());
            throw new IllegalArgumentException("File not found", e);
        } finally {
            deleteSpan.end();
        }
    }
}
