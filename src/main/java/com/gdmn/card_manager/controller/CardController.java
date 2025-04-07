package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.dto.CardData;
import com.gdmn.card_manager.dto.CreateCard;
import com.gdmn.card_manager.enums.LimitType;
import com.gdmn.card_manager.service.CardLimitService;
import com.gdmn.card_manager.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Card controller")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CardController {
    private final CardService cardService;
    private final CardLimitService cardLimitService;

    @Operation(summary = "Create payment card")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCard(@Valid @RequestBody CreateCard createCard) {
        cardService.addPaymentCard(createCard);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Getting payment card by id")
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurity.isCardOwnerOrAdmin(#cardId)")
    public CardData getCardById(@PathVariable("id") long cardId) {
        return cardService.getById(cardId);
    }

    @Operation(summary = "Getting all payment cards")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<CardData> getAllCards(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return cardService.getAll(page, size);
    }

    @Operation(summary = "Getting all payment cards current user")
    @GetMapping("/me")
    public Page<CardData> getAllUserCards(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return cardService.getAllUserCards(page, size);
    }

    @Operation(summary = "Set amount limit of payment card transactions")
    @PatchMapping("/{id}/set_limit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setCardLimit(
            @PathVariable("id") long cardId,
            @RequestParam LimitType limitType,
            @RequestParam @DecimalMin(value = "0.0", message = "The 'amount' must be at least 0") BigDecimal amount) {
        cardLimitService.setCardLimit(cardId, limitType, amount);
    }

    @Operation(summary = "Delete payment card by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCard(@PathVariable(name = "id") long cardId) {
        cardService.deleteCard(cardId);
    }
}
