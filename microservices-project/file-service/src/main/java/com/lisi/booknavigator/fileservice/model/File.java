package com.lisi.booknavigator.fileservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="t_file")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "GCS URL cannot be blank")
    @GCSUrl(message = "Invalid GCS URL format")
    private String url;

    @NotBlank(message = "File type cannot be blank")
    private String fileType;

    @NotNull(message = "Upload date cannot be null")
    private LocalDateTime uploadDate;

    @NotNull(message = "Associated entity ID cannot be null")
    private String associatedEntityId;

    @NotBlank(message = "Associated entity type cannot be blank")
    private String associatedEntityType;

    // Additional fields for indirect reference
    private Long userId; // This can store the User ID from another service

}
