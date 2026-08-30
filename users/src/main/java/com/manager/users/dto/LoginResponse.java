package com.manager.users.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String username;
    private long expiredIn;

    public LoginResponse(
        String token,
        String username,
        long expiredIn
    ) {
        this.token = token;
        this.username = username;
        this.expiredIn = expiredIn;
    }
}
