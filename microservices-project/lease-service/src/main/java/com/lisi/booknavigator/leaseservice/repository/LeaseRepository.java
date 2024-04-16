package com.lisi.booknavigator.leaseservice.repository;

import com.lisi.booknavigator.leaseservice.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
}
