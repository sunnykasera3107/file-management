package com.manager.users.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manager.users.dto.GeneralResponse;
import com.manager.users.dto.GetResponse;
import com.manager.users.dto.RegisterRequest;
import com.manager.users.dto.UserJwt;
import com.manager.users.exception.UserAlreadyExistsException;
import com.manager.users.model.User;
import com.manager.users.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public GeneralResponse createUser(RegisterRequest user)
    {
        boolean isValid = validateUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        
        if (isValid) {
            throw new UserAlreadyExistsException("User account already exist");
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
        
        return new GeneralResponse("Account created successfully");
    }

    public GeneralResponse updateUser(User user) {
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

        return new GeneralResponse( "User account updated successfully");
    }

    public GetResponse getUser(UUID id) {
        User user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return new GetResponse(
            user.getId().toString(),
            user.getFullname(),
            user.getEmail(),
            user.getUsername()
        );
    }

    public GeneralResponse deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new GeneralResponse( " User account deleted successfully");
        }

        throw new UsernameNotFoundException("User does not exist");
    }

    
    public boolean validateUserByUsernameOrEmail(String username, String email) {
        User userExist = userRepository.findByUsernameOrEmail(username, email);
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
                throw new UsernameNotFoundException("User does not exist");
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
