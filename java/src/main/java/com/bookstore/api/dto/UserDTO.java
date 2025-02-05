package com.bookstore.api.dto;

import com.bookstore.api.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
