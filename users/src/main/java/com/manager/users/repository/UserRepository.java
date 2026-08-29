package com.manager.users.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    User findByUsernameAndEmail(String username, String email);
    User findByUsername(String username);
    User findByEmail(String email);
}
