package com.lisi.booknavigator.paymentservice.event;

import com.lisi.booknavigator.paymentservice.model.Payment;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentEvent {
    private Integer  paymentId;
    private EventType eventType;
    private Payment payment;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
