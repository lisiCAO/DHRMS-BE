package com.lisi.booknavigator.paymentservice.dto;

import lombok.Data;

@Data
public class TokenChargeRequest {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;

    private boolean success;

}
