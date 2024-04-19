package com.lisi.booknavigator.fileservice.controller;

import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.service.FileService;
import com.lisi.booknavigator.fileservice.dto.FileRequest;
import com.lisi.booknavigator.fileservice.dto.FileResponse;
import com.lisi.booknavigator.fileservice.service.GoogleCloudStorageService;
import com.lisi.booknavigator.fileservice.service.StorageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    private final FileService fileService;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final StorageService storageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "storage", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "storage")
    @Retry(name = "storage")
    public CompletableFuture<ResponseEntity<String>> uploadFile(@ModelAttribute FileRequest fileRequest) {
        log.info("Attempting to save file");
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ResponseEntity.ok(fileService.saveFile(fileRequest));
            } catch (IOException e) {
                log.error("IOException during file upload: {}", e.getMessage());
                throw new RuntimeException("File upload failed due to internal server error");
            }
        }).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()));
    }

    public CompletableFuture<String> fallbackMethod(FileRequest orderRequest, RuntimeException runtimeException){
        log.info("Cannot Save File Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please upload file after some times!") ;
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> listFiles(@RequestParam(required = false) Long associatedEntityId,
                                                @RequestParam(required = false) String associatedEntityType) {
        log.info("associatedEntityId = {}, associatedEntityType = {}", associatedEntityId, associatedEntityType);

        List<File> files = fileService.listFiles(associatedEntityId, associatedEntityType);

        if (files == null) {

            log.info("No File found in the database");
            return ResponseEntity.notFound().build();
        }
        else {
            log.info("File found in the database");

            List<FileResponse> fileresponses = files.stream()
                    .map(this::convertToFileResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileresponses);
        }
    }

    private FileResponse convertToFileResponse(File file) {
        FileResponse response = new FileResponse();
        response.setId(file.getId());
        response.setPublicUrl(googleCloudStorageService.generateSignedPublicUrl(file.getUrl())); // convert GCS URL to public URL
        response.setGCSUrl(file.getUrl());
        response.setFileName(storageService.getObjectName(file.getUrl())); // convert GCS URL to object name
        response.setUploadDate(file.getUploadDate());
        response.setAssociatedEntityId(file.getAssociatedEntityId());
        response.setAssociatedEntityType(file.getAssociatedEntityType());
        response.setUserId(file.getUserId());
        return response;
    }

    @GetMapping("id/{fileId}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable Long fileId) {
        File file = fileService.getFileById(fileId);
        if (file == null) {

            log.info("File not found, fileId = {} ", fileId);
            return ResponseEntity.notFound().build();

        }
        else {
            log.info("File found, fileId = {} ", fileId);
            FileResponse fileResponse = new FileResponse(
                    file.getId(),
                    googleCloudStorageService.generateSignedPublicUrl(file.getUrl()),
                    file.getUrl(),
                    storageService.getObjectName(file.getUrl()),
                    file.getUploadDate(),
                    file.getAssociatedEntityId(),
                    file.getAssociatedEntityType(),
                    file.getUserId());
            return ResponseEntity.ok(fileResponse);

        }
    }

    @GetMapping("name/{fileName}")
    public ResponseEntity<FileResponse> getFileByName(@PathVariable String fileName) {
        File file = fileService.findByUrlContaining(fileName);
        if (file == null) {

            log.info("File not found, fileName = {} ", fileName);
            return ResponseEntity.notFound().build();

        }
        else {
            log.info("File found, fileName = {} ", fileName);
            FileResponse fileResponse = new FileResponse(
                    file.getId(),
                    googleCloudStorageService.generateSignedPublicUrl(file.getUrl()),
                    file.getUrl(),
                    storageService.getObjectName(file.getUrl()),
                    file.getUploadDate(),
                    file.getAssociatedEntityId(),
                    file.getAssociatedEntityType(),
                    file.getUserId());
            return ResponseEntity.ok(fileResponse);

        }
    }

    @DeleteMapping("/{fileId}")
    @CircuitBreaker(name = "storage", fallbackMethod = "deleteFallback")
    @TimeLimiter(name = "storage")
    @Retry(name = "storage")
    public CompletableFuture<ResponseEntity<Object>> deleteSingleFileById(@PathVariable Long fileId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                fileService.deleteSingleFileById(fileId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (IOException e) {
                log.error("Error deleting file: {}", e.getMessage());
                throw new RuntimeException("Error deleting file due to internal server error", e);
            } catch (IllegalArgumentException e) {
                log.error("Error finding file: {}", e.getMessage());
                throw new IllegalArgumentException("File not found", e);
            }
        }).exceptionally(ex -> {
            if (ex.getCause() instanceof IllegalArgumentException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }

    public CompletableFuture<ResponseEntity<Void>> deleteFallback(Long fileId, Throwable e) {
        log.info("Fallback triggered for deleteSingleFile");
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

}
