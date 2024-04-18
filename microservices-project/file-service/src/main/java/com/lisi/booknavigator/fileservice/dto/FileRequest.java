package com.lisi.booknavigator.fileservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {
    private MultipartFile file;
    private String fileType;
    private String url;
    private LocalDateTime uploadDate;
    private Long associatedEntityId;
    private String associatedEntityType;
    private Long userId;
}
