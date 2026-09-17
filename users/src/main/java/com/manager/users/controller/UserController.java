package com.manager.users.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.users.dto.GeneralResponse;
import com.manager.users.dto.GetResponse;
import com.manager.users.dto.RegisterRequest;
import com.manager.users.model.User;
import com.manager.users.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public GetResponse getUser(
        @PathVariable UUID id
    ) {
        return userService.getUser(id);        
    }

    @PostMapping("/register")
    public GeneralResponse registerUser(
        @RequestBody RegisterRequest user
    ) {
        return userService.createUser(user);
    }

    @PutMapping
    public GeneralResponse updateUser(
        @RequestBody User user
    ) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public GeneralResponse deleteUser(
        @PathVariable UUID id
    ) {
        return userService.deleteUser(id);
    }
}
