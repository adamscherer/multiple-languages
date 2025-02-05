package com.bookstore.api.transformer;

import com.bookstore.api.dto.CartDTO;
import com.bookstore.api.dto.CartItemDTO;
import com.bookstore.api.model.Cart;
import com.bookstore.api.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartTransformer {
    
    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        CartDTO dto = new CartDTO();
        dto.setItems(cart.getItems().stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList()));
        dto.setSubtotal(cart.getSubtotal());
        return dto;
    }
    
    private CartItemDTO toCartItemDTO(CartItem item) {
        if (item == null) {
            return null;
        }
        
        CartItemDTO dto = new CartItemDTO();
        dto.setBookId(item.getBook().getId());
        dto.setBookTitle(item.getBook().getTitle());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
