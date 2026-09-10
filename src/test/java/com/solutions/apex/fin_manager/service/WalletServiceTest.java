package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.dto.WalletResponseDTO;
import com.solutions.apex.fin_manager.exception.InvalidAmountException;
import com.solutions.apex.fin_manager.exception.UserNotFoundException;
import com.solutions.apex.fin_manager.exception.WalletNotFoundException;
import com.solutions.apex.fin_manager.mapper.WalletMapper;
import com.solutions.apex.fin_manager.model.User;
import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.model.WalletCurrency;
import com.solutions.apex.fin_manager.repository.TransactionRepository;
import com.solutions.apex.fin_manager.repository.UserRepository;
import com.solutions.apex.fin_manager.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldTriggerWalletNotFoundException(){
        when(walletRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWallet(1L));
    }

    @Test
    void shouldReturnWallet(){
        Wallet userWallet = Wallet.builder()
                .id(1L)
                .currency(WalletCurrency.EUR)
                .balance(new BigDecimal(150))
                .build();

        WalletResponseDTO expectedDTO = new WalletResponseDTO(1L, WalletCurrency.EUR, new BigDecimal(150));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(userWallet));
        when(walletMapper.entityToDto(any(Wallet.class))).thenReturn(expectedDTO);

        WalletResponseDTO actualDTO = walletService.getWallet(1L);

        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    void shouldTriggerInvalidAmountException(){
        BigDecimal invalidAmount = new BigDecimal("-1");

        Wallet userWallet = Wallet.builder()
                .id(1L)
                .currency(WalletCurrency.EUR)
                .balance(new BigDecimal(150))
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(userWallet));

        assertThrows(InvalidAmountException.class, ()-> walletService.deposit(1L, invalidAmount));
    }

    @Test
    void shouldTriggerUserNotFoundException(){
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> walletService.getAllUserWallets(1L));
    }



    @Test
    void shouldIncreaseBalance(){
        Wallet userWallet = Wallet.builder()
                .id(1L)
                .currency(WalletCurrency.EUR)
                .balance(new BigDecimal(150))
                .build();

        BigDecimal expectedBalance = new BigDecimal(200);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(userWallet));
        walletService.deposit(1L, new BigDecimal(50));

        assertEquals(expectedBalance, userWallet.getBalance());
    }

    @Test
    void shouldDecreaseBalance(){
        Wallet userWallet = Wallet.builder()
                .id(1L)
                .currency(WalletCurrency.EUR)
                .balance(new BigDecimal(150))
                .build();

        BigDecimal expectedBalance = new BigDecimal(100);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(userWallet));
        walletService.withdraw(1L, new BigDecimal(50));

        assertEquals(expectedBalance, userWallet.getBalance());
    }
}