package com.lisi.booknavigator.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageResponse {
    private String url;
    private String fileType;
    private LocalDateTime uploadDate;
    private long fileSize;
}
