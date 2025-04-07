package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User controller")
@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Getting user data by id")
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurity.isDataOwnerOrAdmin(#userId)")
    public UserData getUser(@PathVariable("id") long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Getting user data by id")
    @GetMapping("/me")
    public UserData getUser() {
        return userService.getCurrentUser();
    }

    @Operation(summary = "Enabling an user/account")
    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public void enableUser(@PathVariable Long id) {
        userService.enable(id);
    }

    @Operation(summary = "Disabling an user/account")
    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableUser(@PathVariable Long id) {
        userService.disable(id);
    }

    @Operation(summary = "Deleting user by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteById(userId);
    }
}
