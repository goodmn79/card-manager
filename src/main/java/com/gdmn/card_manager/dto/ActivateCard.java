package com.gdmn.card_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ActivateCard {
    @Schema(description = "user id")
    private long userId;
    @Schema(description = "payment card balance")
    private BigDecimal balance;
}
