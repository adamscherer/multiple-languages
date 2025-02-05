package com.bookstore.api.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private Error error;

    @Data
    public static class Error {
        private String code;
        private String message;
    }

    public static ErrorResponseDTO of(String code, String message) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        response.setError(error);
        return response;
    }
}
