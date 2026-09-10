package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.UserRegistrationRequestDTO;
import com.solutions.apex.fin_manager.dto.UserResponseDTO;
import com.solutions.apex.fin_manager.exception.EmailAlreadyExistsException;
import com.solutions.apex.fin_manager.exception.UserNotFoundException;
import com.solutions.apex.fin_manager.exception.UsernameAlreadyExistsException;
import com.solutions.apex.fin_manager.mapper.UserMapper;
import com.solutions.apex.fin_manager.model.User;
import com.solutions.apex.fin_manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import org.mockito.Mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldTriggerEmailAlreadyExistsException(){
        User user = User.builder()
                .id(1L)
                .email("ivanov@gmail.com")
                .username("ivanov123")
                .passwordHash("somepasswordhash")
                .build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createUser(new UserRegistrationRequestDTO("ivanov123",
                "ivanov@gmail.com",
                "strongasshitpassword")));

    }

    @Test
    void shouldTriggerExistsByUsernameException(){
        User user = User.builder()
                .id(1L)
                .email("ivanov@gmail.com")
                .username("ivanov123")
                .passwordHash("somepasswordhash")
                .build();

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(new UserRegistrationRequestDTO("ivanov123",
                "ivanov@gmail.com",
                "strongasshitpassword")));
    }

    @Test
    void shouldProcessUser(){
        User user = User.builder()
                .id(1L)
                .email("ivanov@gmail.com")
                .username("ivanov123")
                .passwordHash("somepasswordhash")
                .build();

        UserRegistrationRequestDTO dto = new UserRegistrationRequestDTO("anyusername",
                "anyemail@gmail.com",
                "anypassword");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedhash");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDTO expected = new UserResponseDTO(1L, "ivanov123", "ivanov@gmail.com");

        when(userMapper.toDTO(any(User.class)))
                .thenReturn(expected);

        UserResponseDTO actual = userService.createUser(dto);

        assertEquals(expected, actual);
    }

    @Test
    void shouldTriggerUserNotFoundException(){
        User user = User.builder()
                .id(1L)
                .email("ivanov@gmail.com")
                .username("ivanov123")
                .passwordHash("somepasswordhash")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void shouldReturnUser(){
        User user = User.builder()
                .id(1L)
                .email("ivanov@gmail.com")
                .username("ivanov123")
                .passwordHash("somepasswordhash")
                .build();

        UserResponseDTO expectedDto = new UserResponseDTO(1L, "ivanov123", "ivanov@gmail.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(expectedDto);

        UserResponseDTO actualDto = userService.getUserById(1L);
        assertEquals(expectedDto, actualDto);
    }
}