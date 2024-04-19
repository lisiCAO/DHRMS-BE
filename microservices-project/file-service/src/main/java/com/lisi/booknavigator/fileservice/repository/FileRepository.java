package com.lisi.booknavigator.fileservice.repository;

import com.lisi.booknavigator.fileservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    // check if the associatedEntityId exists in the database
    List<File> findByAssociatedEntityId(Long associatedEntityId);

    // check if the associatedEntityId exists in the database
    List<File> findByAssociatedEntityType(String associatedEntityType);

    // check if the url exists in the database
    Optional<File> findByUrl(String url);
}
