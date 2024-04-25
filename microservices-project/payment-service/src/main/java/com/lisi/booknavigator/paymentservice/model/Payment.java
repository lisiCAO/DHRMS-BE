package com.lisi.booknavigator.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;


//@Document(value = "payment")


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long paymentId;
    @NonNull
    private Long leaseId;
    @NonNull
    private Long landLordId;
    @NonNull
    private Long paidByUserId;
    @NonNull
    private float amount;
    private String paymentDate;
    private PaymentType paymentMethod;
    private PaymentStatus status;

}


