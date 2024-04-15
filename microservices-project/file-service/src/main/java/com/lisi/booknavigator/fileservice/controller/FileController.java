package com.lisi.booknavigator.fileservice.controller;

import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.service.FileService;
import com.lisi.booknavigator.fileservice.dto.FileRequest;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    private final FileService fileService;

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

    @GetMapping
    public ResponseEntity<List<File>> listFiles(@RequestParam(required = false) Integer associatedEntityId,
                                                @RequestParam(required = false) String associatedEntityType) {
        List<File> files = fileService.listFiles(associatedEntityId, associatedEntityType);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        return file != null ? ResponseEntity.ok(file) : ResponseEntity.notFound().build();
    }

    public CompletableFuture<String> fallbackMethod(FileRequest orderRequest, RuntimeException runtimeException){
        log.info("Cannot Save File Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please upload file after some times!") ;
    }
}
