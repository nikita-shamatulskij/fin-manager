package com.solutions.apex.fin_manager.controller;

import com.solutions.apex.fin_manager.dto.*;
import com.solutions.apex.fin_manager.service.TransactionService;
import com.solutions.apex.fin_manager.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody @Valid WalletCreateRequestDTO requestDTO) {
        WalletResponseDTO walletResponseDTO = walletService.createWallet(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(walletResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getWalletById(@PathVariable Long id) {
        WalletResponseDTO response = walletService.getWallet(id);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<Page<TransactionResponseDTO>> getWalletHistory (@PathVariable Long id,
                                                                          @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(transactionService.getWalletHistory(id, pageable));
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        walletService.deposit(id, amount);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id,
                                         @RequestBody @Valid WalletBalanceRequestDTO requestDTO) {
        walletService.withdraw(id, requestDTO.amount());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/exchange")
    public ResponseEntity<Void> exchangeCurrency(@RequestBody @Valid WalletExchangeRequestDTO requestDTO) {
        walletService.exchangeCurrency(requestDTO);

        return ResponseEntity.ok().build();
    }
}