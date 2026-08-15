package com.solutions.apex.fin_manager.controller;

import com.solutions.apex.fin_manager.dto.UserRegistrationRequestDTO;
import com.solutions.apex.fin_manager.dto.UserResponseDTO;
import com.solutions.apex.fin_manager.dto.WalletResponseDTO;
import com.solutions.apex.fin_manager.service.UserService;
import com.solutions.apex.fin_manager.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;

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

    @GetMapping("/{id}/wallets")
    public ResponseEntity<List<WalletResponseDTO>> getWallets(@PathVariable Long id){
        List<WalletResponseDTO> response = walletService.getAllUserWallets(id);

        return ResponseEntity.ok().body(response);
    }
}