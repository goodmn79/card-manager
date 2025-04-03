package com.gdmn.card_manager.dto;

import com.gdmn.card_manager.enums.LimitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CardLimitData {
    @Schema(description = "card limit id")
    private long id;
    @Schema(description = "card limit type", allowableValues = {"DAILY_WITHDRAWAL", "MONTHLY_WITHDRAWAL", "DAILY_TRANSFER", "MONTHLY_TRANSFER"})
    private LimitType limitType;
    @Schema(description = "card limit amount")
    private BigDecimal amount;
}
