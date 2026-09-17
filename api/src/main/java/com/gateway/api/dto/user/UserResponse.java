package com.gateway.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String fullname;
}
