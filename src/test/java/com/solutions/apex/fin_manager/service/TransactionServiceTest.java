package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.TransactionCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.TransactionResponseDTO;
import com.solutions.apex.fin_manager.exception.CurrencyMismatchException;
import com.solutions.apex.fin_manager.exception.InsufficientFundsException;
import com.solutions.apex.fin_manager.exception.WalletNotFoundException;
import com.solutions.apex.fin_manager.mapper.TransactionMapper;
import com.solutions.apex.fin_manager.model.Transaction;
import com.solutions.apex.fin_manager.model.User;
import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.model.WalletCurrency;
import com.solutions.apex.fin_manager.repository.TransactionRepository;
import com.solutions.apex.fin_manager.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService service;

    @Test
    void shouldTriggerCurrencyMismatchException(){
        Wallet senderWallet = Wallet.builder()
                .balance(new BigDecimal(200))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        Wallet receiverWallet = Wallet.builder()
                .balance(new BigDecimal(100))
                .user(User.builder()
                        .email("som3email@gmail.com")
                        .id(2L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.USD)
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        TransactionCreateRequestDTO dto = new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(50));

        assertThrows(CurrencyMismatchException.class, () -> service.transferMoney(dto));
    }

    @Test
    void shouldTriggerInsufficientFundsException(){
        Wallet senderWallet = Wallet.builder()
                .balance(new BigDecimal(100))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        Wallet receiverWallet = Wallet.builder()
                .balance(new BigDecimal(200))
                .user(User.builder()
                        .email("som3email@gmail.com")
                        .id(2L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        TransactionCreateRequestDTO dto = new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(150));

        assertThrows(InsufficientFundsException.class, () -> service.transferMoney(dto));
    }

    @Test
    void shouldIncreaseBalanceAtReceiverWallet(){
        Wallet senderWallet = Wallet.builder()
                .balance(new BigDecimal(300))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        Wallet receiverWallet = Wallet.builder()
                .balance(new BigDecimal(200))
                .user(User.builder()
                        .email("som3email@gmail.com")
                        .id(2L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        TransactionCreateRequestDTO dto = new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(150));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        service.transferMoney(dto);

        var receiverBalance = receiverWallet.getBalance();

        assertEquals(new BigDecimal("350"), receiverBalance);

    }

    @Test
    void shouldDecreaseBalanceAtSenderWallet(){
        Wallet senderWallet = Wallet.builder()
                .balance(new BigDecimal(300))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        Wallet receiverWallet = Wallet.builder()
                .balance(new BigDecimal(200))
                .user(User.builder()
                        .email("som3email@gmail.com")
                        .id(2L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        TransactionCreateRequestDTO dto = new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(150));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        service.transferMoney(dto);

        var senderBalance = senderWallet.getBalance();

        assertEquals(new BigDecimal(150), senderBalance);
    }

    @Test
    void shouldCreateTransaction(){
        Wallet senderWallet = Wallet.builder()
                .balance(new BigDecimal(300))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        Wallet receiverWallet = Wallet.builder()
                .balance(new BigDecimal(200))
                .user(User.builder()
                        .email("som3email@gmail.com")
                        .id(2L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        TransactionCreateRequestDTO dto = new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(150));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        service.transferMoney(dto);

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowWhenSenderWalletNotFound(){
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> service.transferMoney(new TransactionCreateRequestDTO(1L, 2L, new BigDecimal(150))));
    }

    @Test
    void shouldReturnWalletHistory(){
        Wallet userWallet = Wallet.builder()
                .balance(new BigDecimal(300))
                .user(User.builder()
                        .email("someemail@gmail.com")
                        .id(1L)
                        .passwordHash("fwfefwfefwfeww")
                        .build())
                .currency(WalletCurrency.EUR)
                .build();

        when(walletRepository.existsById(1L)).thenReturn(true);

        Pageable pageable = PageRequest.of(0, 10);

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .senderWallet(userWallet)
                .receiverWallet(userWallet)
                .amount(new BigDecimal("100"))
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .senderWallet(userWallet)
                .receiverWallet(userWallet)
                .amount(new BigDecimal("50"))
                .build();

        Page<Transaction> page = new PageImpl<>(List.of(transaction1, transaction2), pageable, 2);

        when(transactionRepository
                .findBySenderWalletIdOrReceiverWalletId(1L,
                        1L,
                        pageable)).thenReturn(page);

        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(new TransactionResponseDTO(1L, 1L, 2L, new BigDecimal(150), LocalDateTime.now()));

        Page<TransactionResponseDTO> history = service.getWalletHistory(1L, pageable);

        assertEquals(2, history.getTotalElements());
        assertEquals(2, history.getContent().size());

        verify(transactionRepository).findBySenderWalletIdOrReceiverWalletId(1L, 1L, pageable);
    }
}