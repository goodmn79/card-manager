package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.enums.Role;
import com.gdmn.card_manager.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User map(Register register) {
        String encodedPassword = passwordEncoder.encode(register.getPassword());
        return new User()
                .setUsername(register.getUsername())
                .setPassword(encodedPassword)
                .setFirstName(register.getFirstName())
                .setMidlName(register.getMidlName())
                .setLastName(register.getLastName())
                .setRole(Role.ROLE_USER);
    }

    public UserData map(User user) {
        return new UserData()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setMidlName(user.getMidlName())
                .setLastName(user.getLastName())
                .setEmail(user.getUsername())
                .setRole(user.getRole());
    }
}
