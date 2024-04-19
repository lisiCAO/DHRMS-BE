package com.lisi.booknavigator.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URL;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {
    private Long id;

    private URL publicUrl;

    private String GCSUrl;

    private String fileName;

    private LocalDateTime uploadDate;

    private String associatedEntityId;

    private String associatedEntityType;

    private Long userId;
}