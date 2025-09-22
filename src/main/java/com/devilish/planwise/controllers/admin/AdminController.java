package com.devilish.planwise.controllers.admin;

import com.devilish.planwise.dto.auth.RegisterRequest;
import com.devilish.planwise.dto.user.UserResponse;
import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody RegisterRequest request) {
        // Verificar se o email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        // Criar novo usuário ADMIN
        User admin = new User();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(User.Role.ADMIN);
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());

        User savedAdmin = userRepository.save(admin);

        return ResponseEntity.ok(UserResponse.fromUser(savedAdmin));
    }

    @PostMapping("/promote-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> promoteUserToAdmin(@PathVariable Long userId) {
        return userRepository.findById(userId)
            .map(user -> {
                user.setRole(User.Role.ADMIN);
                User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
