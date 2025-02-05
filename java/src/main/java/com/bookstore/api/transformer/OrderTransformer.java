package com.bookstore.api.transformer;

import com.bookstore.api.dto.OrderDTO;
import com.bookstore.api.dto.OrderItemDTO;
import com.bookstore.api.model.Order;
import com.bookstore.api.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderTransformer {
    
    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setItems(order.getItems().stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingCost(order.getShippingCost());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    private OrderItemDTO toDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setBookId(item.getBook().getId());
        dto.setBookTitle(item.getBook().getTitle());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtTime(item.getPriceAtTime());
        return dto;
    }
}
