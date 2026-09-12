package com.manager.users.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manager.users.model.UserJwt;
import com.manager.users.dto.RegisterRequest;
import com.manager.users.model.User;
import com.manager.users.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, String> createUser(RegisterRequest user) 
        throws Exception
    {
        boolean isValid = validateUserByUsernameAndEmail(user.getUsername(), user.getEmail());
        
        if (isValid) {
            return Map.of("message", "User account already exist");
        }
        
        User newUser = new User(
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getFullname()
        );

        newUser.setPassword(
            passwordEncoder.encode(user.getPassword())
        );

        newUser.setPhone(user.getPhone());
        userRepository.save(newUser);
        
        return Map.of("message", "Account created successfully");
    }

    public Map<String, String> updateUser(User user) {
        User userExist = userRepository
            .findById(user.getId())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!userExist.getUsername().equals(user.getUsername())){
            userExist.setUsername(user.getUsername());
        }

        if (!userExist.getFullname().toLowerCase().equals(
            user.getFullname().toLowerCase()
        )) {
            userExist.setFullname(user.getFullname());
        }

        if (!userExist.getEmail().equals(user.getEmail())) {
            userExist.setEmail(user.getEmail());
        }

        if (!user.getPassword().isBlank()) {
            userExist.setPassword(
                passwordEncoder.encode(user.getPassword())
            );
        }

        if (!userExist.getPhone().equals(user.getPhone())) {
            userExist.setPhone(user.getPhone());
        }

        userRepository.save(userExist);

        return Map.of("message", "User account updated successfully");
    }

    public Map<String, String> deleteUser(UUID id) {
        if (validateUserById(id)) {
            userRepository.deleteById(id);
            return Map.of("message", " User account deleted successfully");
        }

        return Map.of("message", "User account not found");
    }

    public boolean validateUserById(UUID id) {
        Optional<User> userExist = userRepository.findById(id);
        return userExist.isPresent() ? true : false;
    }

    public boolean validateUserByEmail(String email) {
        User userExist = userRepository.findByEmail(email);
        return userExist != null ? true : false;
    }

    public boolean validateUserByUsername(String username) {
        User userExist = userRepository.findByUsername(username);
        return userExist != null ? true : false;
    }

    public boolean validateUserByUsernameAndEmail(String username, String email) {
        User userExist = userRepository.findByUsernameAndEmail(username, email);
        return userExist != null ? true : false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository
            .findByUsername(username);
        
        if (user == null) {
            user = userRepository
                .findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
        }

        return new UserJwt(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("USER"))
        );
    }
        
}
