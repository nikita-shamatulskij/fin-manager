package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.UserRegistrationRequestDTO;
import com.solutions.apex.fin_manager.dto.UserResponseDTO;
import com.solutions.apex.fin_manager.exception.EmailAlreadyExistsException;
import com.solutions.apex.fin_manager.exception.UserNotFoundException;
import com.solutions.apex.fin_manager.exception.UsernameAlreadyExistsException;
import com.solutions.apex.fin_manager.mapper.UserMapper;
import com.solutions.apex.fin_manager.model.User;
import com.solutions.apex.fin_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO createUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        if (userRepository.existsByEmail(userRegistrationRequestDTO.email())){
            throw new EmailAlreadyExistsException("Email: " + userRegistrationRequestDTO.email() + " уже занят!");
        }

        if (userRepository.existsByUsername(userRegistrationRequestDTO.username())){
            throw new UsernameAlreadyExistsException("Username: " + userRegistrationRequestDTO.username() + " уже занят!");
        }

        User user = userMapper.toEntity(userRegistrationRequestDTO);
        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + id + " не найден"));
    }
}