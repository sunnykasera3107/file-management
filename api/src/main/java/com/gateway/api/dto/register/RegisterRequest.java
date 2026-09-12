package com.gateway.api.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User registration request information")
public class RegisterRequest {
    
    @Schema(description = "User full name", example = "John Bert", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String fullname;
    
    @Schema(
        description = "Unique username", example="john.b", requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String username;
    
    @Schema(description = "Unique email address", example = "john.b@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    private String email;

    @Schema(
        description = "A Strong password of length 8 - 12 digits, letters, and a special char (@.) accepted.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;


    @Schema(
        description = "A phone number"
    )
    private String phone;
}
