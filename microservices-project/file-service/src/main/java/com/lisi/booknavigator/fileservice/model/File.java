package com.lisi.booknavigator.fileservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

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
    private Integer id;

    @NotBlank(message = "URL cannot be blank")
    @URL(message = "Invalid URL format")
    private String url;

    @NotBlank(message = "File type cannot be blank")
    private String fileType;

    @NotNull(message = "Upload date cannot be null")
    private LocalDateTime uploadDate;

    @NotNull(message = "Associated entity ID cannot be null")
    private Integer associatedEntityId;

    @NotBlank(message = "Associated entity type cannot be blank")
    private String associatedEntityType;

    // Additional fields for indirect reference
    private Integer userId; // This can store the User ID from another service

}
