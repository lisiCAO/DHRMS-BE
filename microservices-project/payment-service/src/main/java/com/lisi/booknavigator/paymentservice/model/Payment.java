package com.lisi.booknavigator.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Payment {
    @Id
    private Integer paymentId;
    private Integer leaseId;
    private Integer paidByUserId;
    private float amount;
    private String paymentDate;
    private PaymentType paymentMethod;
    private PaymentStatus status;

}


