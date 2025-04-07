package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.exseption.UserNotFoundException;
import com.gdmn.card_manager.exseption.UserStatusConflictException;
import com.gdmn.card_manager.exseption.UsernameAlreadyExistsException;
import com.gdmn.card_manager.mapper.UserMapper;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.repository.UserRepository;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void addUser(Register register) {
        log.info("Invoke method '#addUser'");
        if (this.existsByUsername(register.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User user = userMapper.map(register);
        userRepository.save(user);
    }

    @Override
    public void disable(long id) {
        log.info("Invoke method '#disable'");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isEnabled()) {
            throw new UserStatusConflictException("User account already disable");
        }
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void enable(long id) {
        log.info("Invoke method '#enable'");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isEnabled()) {
            throw new UserStatusConflictException("User account already enable");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public User getById(long id) {
        log.info("Invoke method '#getById'");
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User currentUser() {
        log.info("Invoke method '#currentUser'");
        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserData getUserById(long id) {
        log.info("Invoke method '#getUserById'");
        return userMapper.map(this.getById(id));
    }

    @Override
    public UserData getCurrentUser() {
        log.info("Invoke method '#getCurrentUser'");
        return userMapper.map(this.currentUser());
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("Invoke method '#existsByUsername'");
        return userRepository.existsByUsername(username);
    }

    @Override
    public User getByUsername(String username) {
        log.info("Invoke method '#getByUsername'");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void deleteById(long id) {
        log.info("Invoke method '#deleteById'");
        User user =
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
