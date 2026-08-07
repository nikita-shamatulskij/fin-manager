package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.TransactionCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.TransactionResponseDTO;
import com.solutions.apex.fin_manager.mapper.TransactionMapper;
import com.solutions.apex.fin_manager.model.Transaction;
import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.repository.TransactionRepository;
import com.solutions.apex.fin_manager.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletRepository walletRepository;

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionCreateRequestDTO transactionCreateRequestDTO) {

        Wallet senderWallet = walletRepository.findById(transactionCreateRequestDTO.senderWalletId())
                .orElseThrow(() -> new RuntimeException("Кошелек не найден"));

        Wallet receiverWallet = walletRepository.findById(transactionCreateRequestDTO.receiverWalletId())
                .orElseThrow(() -> new RuntimeException("Кошелек не найден"));

        if (!receiverWallet.getCurrency().equals(senderWallet.getCurrency())) {
            throw new RuntimeException("Валюта кошелька отправителя и получателя не совпадают!");
        }

        if (senderWallet.getBalance().compareTo(transactionCreateRequestDTO.amount()) < 0) {
            throw new RuntimeException("Недостаточно средств");
        }

        BigDecimal amount = transactionCreateRequestDTO.amount();

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        Transaction transaction = Transaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(amount)
                .build();

        Transaction savedTransactional = transactionRepository.save(transaction);

        return transactionMapper.toDTO(savedTransactional);
    }
}