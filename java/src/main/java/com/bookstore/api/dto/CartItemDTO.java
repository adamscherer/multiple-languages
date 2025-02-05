package com.bookstore.api.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
}
