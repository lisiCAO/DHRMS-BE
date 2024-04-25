package com.lisi.booknavigator.paymentservice.dto;


import com.lisi.booknavigator.paymentservice.model.PaymentStatus;
import com.lisi.booknavigator.paymentservice.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
        private Long paymentId;
        private Long leaseId;
        private Long landLordId;
        private Long paidByUserId;
        private float amount;
        private String paymentDate;
        private PaymentType paymentMethod;
        private PaymentStatus status;
}
