package com.gdmn.card_manager.dto;

import com.gdmn.card_manager.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CardData {
    @Schema(description = "payment card id")
    private long id;
    @Schema(description = "payment card number")
    private String cardNumber;
    @Schema(description = "user id")
    private long userId;
    @Schema(description = "payment card expiry date")
    private LocalDate expiryDate;
    @Schema(description = "payment card status", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    private CardStatus status;
    @Schema(description = "payment card balance")
    private BigDecimal balance;
    @Schema(description = "list of limits for operations")
    private Set<CardLimitData> limits = new HashSet<>();
}
