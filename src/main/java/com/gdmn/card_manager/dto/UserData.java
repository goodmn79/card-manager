package com.gdmn.card_manager.dto;

import com.gdmn.card_manager.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserData {
    @Schema(description = "user id")
    private long id;
    @Schema(description = "user first name")
    private String firstName;
    @Schema(description = "user midl name")
    private String midlName;
    @Schema(description = "user last name")
    private String lastName;
    @Schema(description = "user email")
    private String email;
    @Schema(description = "user role", allowableValues = {"ROLE_ADMIN", "ROLE_USER"})
    private Role role;
}
