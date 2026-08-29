package com.manager.users.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    private UUID id;

    @NotNull(message = "Username cannot be null")
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull(message = "Email cannot be null")
    @Column(nullable = false, unique =  true)
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Fullname cannot be null")
    @Column(nullable = false)
    private String fullname;

    @Column(nullable = true)
    private String phone;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public User() {
    }

    public User(@NotNull(message = "Username cannot be null") String username,
            @NotNull(message = "Email cannot be null") String email,
            @NotNull(message = "Password cannot be null") @Size(min = 6) String password,
            @NotNull(message = "Fullname cannot be null") String fullname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }
  
}
