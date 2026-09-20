package com.manager.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.manager.users.dto.GetResponse;
import com.manager.users.model.User;
import com.manager.users.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserWhenUserExists() {

        UUID id = UUID.randomUUID();

        User user = new User(
            "mocker@unittest.com", 
            "mocker@unittest.com", 
            "mocker@pass", 
            "Mocker Test"
        );
        user.setId(id);
        user.setPhone("+111111111111");

        // Arrange
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        // Act
        GetResponse result = userService.getUser(id);

        // Assert
        assertEquals("mocker@unittest.com", result.getEmail());

        // Verify interaction
        verify(userRepository).findById(id);
    }
    
}
