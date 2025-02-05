package com.bookstore.api.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long orderId;
    private String paymentMethod;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
}
