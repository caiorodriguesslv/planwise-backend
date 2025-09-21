package com.devilish.planwise.dto;

import com.devilish.planwise.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private Boolean active;
    private String role;
    
    public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getActive(),
            user.getRole().name()
        );
    }
}
