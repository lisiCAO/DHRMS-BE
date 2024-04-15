package com.lisi.booknavigator.fileservice.service;

import com.lisi.booknavigator.fileservice.event.FileSavedEvent;
import com.lisi.booknavigator.fileservice.model.File;
import com.lisi.booknavigator.fileservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private FileRepository fileRepository;
    private final StorageService storageService;
    private final KafkaTemplate<String, FileSavedEvent> kafkaTemplate;

    public File saveFile(String url, String fileType, LocalDateTime uploadDate, Integer associatedEntityId, String associatedEntityType, Integer userId) {
        File file = new File();
        file.setUrl(url);
        file.setFileType(fileType);
        file.setUploadDate(uploadDate);
        file.setAssociatedEntityId(associatedEntityId);
        file.setAssociatedEntityType(associatedEntityType);
        file.setUserId(userId);
        return fileRepository.save(file);
    }

    public List<File> listFiles(Integer associatedEntityId, String associatedEntityType) {
        // Here we might need to add logic to filter based on parameters
        return fileRepository.findAll();
    }

    public File getFileById(Integer fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}
