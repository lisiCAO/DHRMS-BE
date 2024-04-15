package com.lisi.booknavigator.fileservice.controller;

import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.service.FileService;
import com.lisi.booknavigator.fileservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("associatedEntityId") Integer associatedEntityId,
                                           @RequestParam("associatedEntityType") String associatedEntityType,
                                           @RequestParam("userId") Integer userId,
                                           @RequestParam("bucketName") String bucketName) {
        try {
            String fileUrl = storageService.uploadFile(file, bucketName);
            File savedFile = fileService.saveFile(fileUrl, file.getContentType(), LocalDateTime.now(), associatedEntityId, associatedEntityType, userId);
            return ResponseEntity.created(null).body(savedFile);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
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

}
