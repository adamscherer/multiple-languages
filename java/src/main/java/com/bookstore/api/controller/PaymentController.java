package com.bookstore.api.controller;

import com.bookstore.api.dto.PaymentRequestDTO;
import com.bookstore.api.dto.PaymentResponseDTO;
import com.bookstore.api.model.Order;
import com.bookstore.api.model.User;
import com.bookstore.api.repository.OrderRepository;
import com.bookstore.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public PaymentController(OrderRepository orderRepository,
                           UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        User user = userService.getCurrentUser();
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ensure the order belongs to the current user
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Simulate payment processing
        // In a real application, this would integrate with a payment gateway
        String transactionId = UUID.randomUUID().toString();
        
        // Update order status
        order.setStatus("PAID");
        orderRepository.save(order);

        return ResponseEntity.ok(PaymentResponseDTO.success(transactionId));
    }
}
