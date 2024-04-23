package com.lisi.booknavigator.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lisi.booknavigator.paymentservice.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // You can add custom query methods here if needed
}