package com.manager.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    
    @NotBlank
    private String fullname;
    
    @NotBlank
    private String username;
    
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String phone;

}
