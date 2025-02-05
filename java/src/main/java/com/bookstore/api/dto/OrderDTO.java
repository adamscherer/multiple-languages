package com.bookstore.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private List<OrderItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
}
