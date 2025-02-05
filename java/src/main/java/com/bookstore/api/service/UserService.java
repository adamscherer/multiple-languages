package com.bookstore.api.service;

import com.bookstore.api.dto.UserDTO;
import com.bookstore.api.model.User;
import com.bookstore.api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO getCurrentUserDTO() {
        return UserDTO.fromUser(getCurrentUser());
    }
}
