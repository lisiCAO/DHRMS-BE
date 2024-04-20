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
        private Integer paymentId;
        private Integer leaseId;
        private Integer paidByUserId;
        private float amount;
        private String paymentDate;
        private PaymentType paymentMethod;
        private PaymentStatus status;
}
