package com.gdmn.card_manager.dto;

import com.gdmn.card_manager.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
public class TransactionData {
    @Schema(description = "Transaction id")
    private Long id;
    @Schema(description = "Payment card id")
    private long cardId;
    @Schema(description = "Transaction type")
    private TransactionType type;
    @Schema(description = "Transaction date, time")
    private LocalDateTime timestamp;
    @Schema(description = "Transaction amount")
    private BigDecimal amount;
    @Schema(description = "Transaction description")
    private String description;
}
