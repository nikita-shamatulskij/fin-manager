package com.solutions.apex.fin_manager.controller;

import com.solutions.apex.fin_manager.dto.UserRegistrationRequestDTO;
import com.solutions.apex.fin_manager.dto.UserResponseDTO;
import com.solutions.apex.fin_manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegistrationRequestDTO requestDTO){
        UserResponseDTO responseDTO = userService.createUser(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id){
        UserResponseDTO responseDTO = userService.getUserById(id);

        return ResponseEntity.ok(responseDTO);
    }
}