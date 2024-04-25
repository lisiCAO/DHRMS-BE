package com.lisi.booknavigator.paymentservice.dto;


import com.lisi.booknavigator.paymentservice.model.PaymentStatus;
import com.lisi.booknavigator.paymentservice.model.PaymentType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

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
