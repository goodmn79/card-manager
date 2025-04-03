package com.gdmn.card_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCard {
    @Schema(description = "payment card number")
    private String cardNumber;
}
