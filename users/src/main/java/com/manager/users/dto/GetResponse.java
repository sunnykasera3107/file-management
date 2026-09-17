package com.manager.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
public class GetResponse {
    private String id;
    private String username;
    private String email;
    private String fullname;
}
