package com.gateway.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Schema(description = "User login request schema")
public class LoginRequest {
    
    @Schema(description = "Username or Email address", example = "john.b@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String username;

    @Schema(description = "Password for login", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}
