package com.lisi.booknavigator.paymentservice.repository;

import com.lisi.booknavigator.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, Integer> {
}
