package com.bookstore.api.controller;

import com.bookstore.api.dto.OrderDTO;
import com.bookstore.api.model.Book;
import com.bookstore.api.model.Cart;
import com.bookstore.api.model.Order;
import com.bookstore.api.model.OrderItem;
import com.bookstore.api.model.User;
import com.bookstore.api.repository.OrderRepository;
import com.bookstore.api.service.UserService;
import com.bookstore.api.transformer.OrderTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderTransformer orderTransformer;

    public OrderController(OrderRepository orderRepository,
                         UserService userService,
                         OrderTransformer orderTransformer) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderTransformer = orderTransformer;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder() {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        final Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPriceAtTime(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        order.updateTotals();
        
        Order savedOrder = orderRepository.save(order);
        
        // Clear the cart
        cart.getItems().clear();
        cart.updateSubtotal();

        return ResponseEntity.ok(orderTransformer.toDTO(savedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> listOrders() {
        User user = userService.getCurrentUser();
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        
        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderTransformer::toDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        User user = userService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ensure the order belongs to the current user
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return ResponseEntity.ok(orderTransformer.toDTO(order));
    }
}
