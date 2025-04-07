package com.gdmn.card_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Register {
    @Email(message = "The 'username' should be email")
    @NotBlank(message = "The 'username' cannot be null or Blank")
    @Schema(description = "username: email")
    private String username;

    @NotBlank(message = "The 'password' cannot be null or Blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,30}$", message = "The 'password' does not correspond to the format")
    @Schema(description = "password")
    private String password;

    @NotBlank(message = "The 'firstName' cannot be null or Blank")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+(?:[ -][A-Za-zА-Яа-яЁё]+)*$", message = "The 'firstName' does not correspond to the format")
    @Schema(description = "user first name")
    private String firstName;

    @Nullable
    @Pattern(regexp = "^(?:[A-Za-zА-Яа-яЁё]+(?:[ -][A-Za-zА-Яа-яЁё]+)*|)$", message = "The 'midlName' does not correspond to the format")
    @Schema(description = "user midl name")
    private String midlName;

    @NotBlank(message = "The 'lastName' cannot be null or Blank")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+(?:[ -][A-Za-zА-Яа-яЁё]+)*$", message = "The 'lastName' does not correspond to the format")
    @Schema(description = "user last name")
    private String lastName;
}
