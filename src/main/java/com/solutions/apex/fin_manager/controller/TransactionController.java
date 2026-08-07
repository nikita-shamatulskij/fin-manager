package com.solutions.apex.fin_manager.controller;

import com.solutions.apex.fin_manager.dto.TransactionCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.TransactionResponseDTO;
import com.solutions.apex.fin_manager.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> transferMoney(@RequestBody @Valid TransactionCreateRequestDTO requestDTO){
        TransactionResponseDTO responseDTO =  transactionService.transferMoney(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}