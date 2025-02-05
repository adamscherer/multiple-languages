package com.bookstore.api.controller;

import com.bookstore.api.dto.CartDTO;
import com.bookstore.api.model.Book;
import com.bookstore.api.model.Cart;
import com.bookstore.api.model.CartItem;
import com.bookstore.api.model.User;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.repository.CartRepository;
import com.bookstore.api.service.UserService;
import com.bookstore.api.transformer.CartTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CartTransformer cartTransformer;

    public CartController(CartRepository cartRepository, BookRepository bookRepository,
                         UserService userService, CartTransformer cartTransformer) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.cartTransformer = cartTransformer;
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal User user) {
        Cart cart = getOrCreateCart(user);
        return ResponseEntity.ok(cartTransformer.toDTO(cart));
    }

    @PostMapping
    public ResponseEntity<CartDTO> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {
        Cart cart = getOrCreateCart(user);
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        final Cart finalCart = cart;
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(finalCart);
                    newItem.setBook(book);
                    finalCart.getItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(quantity);
        cart.updateSubtotal();
        cart = cartRepository.save(cart);

        return ResponseEntity.ok(cartTransformer.toDTO(cart));
    }

    @PutMapping
    public ResponseEntity<CartDTO> updateCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {
        Cart cart = getOrCreateCart(user);
        
        cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    cart.updateSubtotal();
                    cartRepository.save(cart);
                });

        return ResponseEntity.ok(cartTransformer.toDTO(cart));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long bookId) {
        Cart cart = getOrCreateCart(user);
        
        cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        cart.updateSubtotal();
        cart = cartRepository.save(cart);

        return ResponseEntity.ok(cartTransformer.toDTO(cart));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}
