package com.lisi.booknavigator.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;



//@Document(value = "payment")
import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;
    private Long leaseId;
    private Long landLordId;
    private Long paidByUserId;
    private float amount;
    private String paymentDate;
    private PaymentType paymentMethod;
    private PaymentStatus status;

}


