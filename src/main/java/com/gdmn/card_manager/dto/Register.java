package com.gdmn.card_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Register {
    @Schema(description = "username: email")
    private String username;
    @Schema(description = "password")
    private String password;
    @Schema(description = "user first name")
    private String firstName;
    @Schema(description = "user midl name")
    private String midlName;
    @Schema(description = "user last name")
    private String lastName;
}
