package com.solutions.apex.fin_manager.controller;

import com.solutions.apex.fin_manager.dto.WalletCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.WalletResponseDTO;
import com.solutions.apex.fin_manager.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody @Valid WalletCreateRequestDTO requestDTO) {
        WalletResponseDTO walletResponseDTO = walletService.createWallet(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(walletResponseDTO);
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        walletService.deposit(id, amount);

        return ResponseEntity.ok().build();
    }
}
