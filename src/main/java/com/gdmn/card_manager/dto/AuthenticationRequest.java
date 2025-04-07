package com.gdmn.card_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationRequest {
    @Email(message = "Field 'username' should be email")
    @NotBlank(message = "Field 'username' cannot be null or Blank")
    @Schema(description = "username")
    private String username;

    @NotBlank(message = "The 'password' cannot be null or Blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,30}$", message = "The 'password' does not correspond to the format")
    @Schema(description = "password")
    private String password;
}
