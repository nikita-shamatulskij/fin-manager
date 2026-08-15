package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.TransactionCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.TransactionResponseDTO;
import com.solutions.apex.fin_manager.exception.CurrencyMismatchException;
import com.solutions.apex.fin_manager.exception.InsufficientFundsException;
import com.solutions.apex.fin_manager.exception.WalletNotFoundException;
import com.solutions.apex.fin_manager.mapper.TransactionMapper;
import com.solutions.apex.fin_manager.model.Transaction;
import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.repository.TransactionRepository;
import com.solutions.apex.fin_manager.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с ID: " + transactionCreateRequestDTO.senderWalletId() + " не найден"));

        Wallet receiverWallet = walletRepository.findById(transactionCreateRequestDTO.receiverWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с ID: " + transactionCreateRequestDTO.receiverWalletId() + " не найден"));

        if (!receiverWallet.getCurrency().equals(senderWallet.getCurrency())) {
            throw new CurrencyMismatchException("Валюты кошельков не совпадают!");
        }

        if (senderWallet.getBalance().compareTo(transactionCreateRequestDTO.amount()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
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

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getWalletHistory(Long walletId, Pageable pageable){
        if (!walletRepository.existsById(walletId)) {
            throw new WalletNotFoundException("Кошелек с ID: " + walletId + " не найден");
        }

        Page<Transaction> transactions = transactionRepository
                .findBySenderWalletIdOrReceiverWalletId(walletId, walletId, pageable);

        return transactions.map(transactionMapper::toDTO);
    }
}