package com.lisi.booknavigator.leaseservice.repository;

import com.lisi.booknavigator.leaseservice.model.LeaseApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseApplicationRepository extends JpaRepository<LeaseApplication, Long> {
}
