package com.bookstore.api.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private String status;
    private String transactionId;
    
    public static PaymentResponseDTO success(String transactionId) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setStatus("SUCCESS");
        response.setTransactionId(transactionId);
        return response;
    }
}
