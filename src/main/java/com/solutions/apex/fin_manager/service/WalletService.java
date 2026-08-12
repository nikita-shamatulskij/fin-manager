package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.WalletCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.WalletResponseDTO;
import com.solutions.apex.fin_manager.exception.InsufficientFundsException;
import com.solutions.apex.fin_manager.exception.InvalidAmountException;
import com.solutions.apex.fin_manager.exception.UserNotFoundException;
import com.solutions.apex.fin_manager.exception.WalletNotFoundException;
import com.solutions.apex.fin_manager.mapper.WalletMapper;
import com.solutions.apex.fin_manager.model.User;
import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.repository.UserRepository;
import com.solutions.apex.fin_manager.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final WalletMapper walletMapper;

    @Transactional
    public WalletResponseDTO createWallet(WalletCreateRequestDTO createRequestDTO){

        if (!userRepository.existsById(createRequestDTO.userId())){
            throw new UserNotFoundException("Пользователь с ID: " + createRequestDTO.userId() + " не найден");
        }

        User userRef = userRepository.getReferenceById(createRequestDTO.userId());

        Wallet userWallet = Wallet.builder()
                .user(userRef)
                .currency(createRequestDTO.currency())
                .balance(BigDecimal.ZERO)
                .build();

        walletRepository.save(userWallet);

        return walletMapper.entityToDto(userWallet);
    }

    @Transactional
    public void deposit(Long walletId, BigDecimal amount){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с ID: " + walletId + " не найден"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Сумма пополнения должна быть больше нуля");
        }

        wallet.setBalance(wallet.getBalance().add(amount));

        walletRepository.save(wallet);
    }

    @Transactional
    public void withdraw(Long walletId, BigDecimal amount){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с ID: " + walletId + " не найден"));

        if (amount.compareTo(wallet.getBalance()) > 0){
            throw new InsufficientFundsException("Сумма списания больше баланса");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        walletRepository.save(wallet);
    }
}