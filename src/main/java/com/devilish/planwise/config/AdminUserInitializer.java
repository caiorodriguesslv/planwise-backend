package com.devilish.planwise.config;

import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.user.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdminIfNotExists();
    }

    private void createDefaultAdminIfNotExists() {
        // Configurações do admin via variáveis de ambiente
        String adminEmail = dotenv.get("ADMIN_EMAIL", "admin@planwise.com");
        String adminPassword = dotenv.get("ADMIN_PASSWORD", "admin123");
        String adminName = dotenv.get("ADMIN_NAME", "Administrador");
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            
            if (adminPassword.equals("admin123")) {
                // IMPORTANTE: Você está usando a senha padrão! Configure ADMIN_PASSWORD no .env
            }
        } else {
        }
    }
}
