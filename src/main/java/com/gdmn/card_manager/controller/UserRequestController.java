package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.model.UserRequest;
import com.gdmn.card_manager.service.UserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User request controller")
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class UserRequestController {
    private final UserRequestService userRequestService;

    @Operation(summary = "Request for changing payment card status by id")
    @ApiResponse(responseCode = "202")
    @PatchMapping("/{id}")
    @PreAuthorize("@customSecurity.isCardOwnerOrAdmin(#cardId)")
    public ResponseEntity<?> changeCardStatusRequest(@PathVariable("id") long cardId) {
        userRequestService.changeStatusRequest(cardId);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get pending requests")
    @GetMapping("/requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserRequest> getPendingRequests() {
        return userRequestService.getPendingRequests();
    }

    @Operation(summary = "Changing the card status")
    @PostMapping("/{id}/process")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void processRequest(
            @PathVariable Long id,
            @RequestParam boolean approve) {
        userRequestService.changeStatusProcess(id, approve);
    }
}
