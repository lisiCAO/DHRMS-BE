package com.lisi.booknavigator.fileservice.repository;

import com.lisi.booknavigator.fileservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    // check if the associatedEntityId exists in the database
    List<File> findByAssociatedEntityId(String associatedEntityId, Sort sort);

    // check if the associatedEntityId exists in the database
    List<File> findByAssociatedEntityType(String associatedEntityType, Sort sort);

    // check if the associatedEntityId and associatedEntityType exists in the database
    List<File> findByAssociatedEntityIdAndAssociatedEntityType(String associatedEntityId, String associatedEntityType, Sort sort);

    // check if the url exists in the database
    List<File> findByUrlContaining(String fileName);

    // check if the url exists in the database
    Optional<File> findByUrl(String url);
}
