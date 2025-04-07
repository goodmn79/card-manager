package com.gdmn.card_manager.init;

import com.gdmn.card_manager.enums.Role;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@DependsOn("liquibase")
@Component
@RequiredArgsConstructor
public class AdminInit {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${initial.admin.username}")
    private String username;
    @Value("${initial.admin.password}")
    private String password;
    @Value("${initial.admin.first_name}")
    private String firstName;
    @Value("${initial.admin.midl_name}")
    private String midlName;
    @Value("${initial.admin.last_name}")
    private String lastName;

    @PostConstruct
    public void initAdmin() {
        log.info("Initializing Admin");
        if (!userRepository.existsByUsername(username)) {
            User admin = new User()
                    .setUsername(username)
                    .setPassword(passwordEncoder.encode(password))
                    .setFirstName(firstName)
                    .setMidlName(midlName)
                    .setLastName(lastName)
                    .setRole(Role.ROLE_ADMIN);

            userRepository.save(admin);
            log.info("Admin with loin '{}' created", username);
        } else {
            log.info("Admin with loin '{}' already exists", username);
        }
    }
}
