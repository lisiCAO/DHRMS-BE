package com.lisi.booknavigator.fileservice.repository;

import com.lisi.booknavigator.fileservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    // 查询特定关联实体ID的文件
    List<File> findByAssociatedEntityId(Integer associatedEntityId);

    // 查询特定关联实体类型的文件
    List<File> findByAssociatedEntityType(String associatedEntityType);
}
