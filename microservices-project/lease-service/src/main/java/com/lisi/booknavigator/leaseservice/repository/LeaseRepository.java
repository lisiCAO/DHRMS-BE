package com.lisi.booknavigator.leaseservice.repository;

import com.lisi.booknavigator.leaseservice.model.Lease;
import com.lisi.booknavigator.leaseservice.model.LeaseApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
    List<Lease> findByLeaseStatus(String status);
}
