package com.gdmn.card_manager.dto;

import com.gdmn.card_manager.validator.ValidCardNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCard {
    @NotBlank(message = "The 'cardNumber' cannot be null or Blank")
    @ValidCardNumber(message = "The 'cardNumber' does not correspond to the format")
    @Schema(description = "payment card number")
    private String cardNumber;
    @Schema(description = "id card owner")
    private long userId;
}
