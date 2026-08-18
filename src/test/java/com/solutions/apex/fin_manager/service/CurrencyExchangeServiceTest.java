package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.client.NbrbClient;
import com.solutions.apex.fin_manager.dto.NbrbRateResponseDTO;
import com.solutions.apex.fin_manager.model.WalletCurrency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeServiceTest {

    @Mock
    private NbrbClient  nbrbClient;

    @InjectMocks
    private CurrencyExchangeService service;

    @Test
    void shouldReturnSameAmountWhenCurrenciesAreEqual(){
        BigDecimal amount = new BigDecimal("100");

        BigDecimal result = service.convert(amount,
                WalletCurrency.BYN,
                WalletCurrency.BYN);

        assertEquals(amount, result);
        verifyNoInteractions(nbrbClient);
    }

    @Test
    void shouldConvertBynToUsd(){
        NbrbRateResponseDTO rate = new NbrbRateResponseDTO("USD", 1, new BigDecimal("2"));
        BigDecimal amount = new BigDecimal("100");

        when(nbrbClient.getExchangeRate("USD")).thenReturn(rate);

        BigDecimal result = service.convert(amount, WalletCurrency.BYN, WalletCurrency.USD);

        assertEquals(new BigDecimal("50.0000"), result);
        verify(nbrbClient).getExchangeRate("USD");
    }

    @Test
    void shouldConvertUsdToByn(){
        NbrbRateResponseDTO rate  = new NbrbRateResponseDTO("USD", 1, new BigDecimal("2"));
        BigDecimal amount = new BigDecimal("100");

        when(nbrbClient.getExchangeRate("USD")).thenReturn(rate);

        BigDecimal result = service.convert(amount, WalletCurrency.USD, WalletCurrency.BYN);

        assertEquals(new BigDecimal("200.0000"), result);
        verify(nbrbClient).getExchangeRate("USD");
    }

    @Test
    void shouldConvertUsdToEur(){
        NbrbRateResponseDTO fromRate =  new NbrbRateResponseDTO("USD", 1, new BigDecimal("2"));
        NbrbRateResponseDTO toRate =  new NbrbRateResponseDTO("EUR", 1, new BigDecimal("3"));

        BigDecimal amount = new BigDecimal("100");

        when(nbrbClient.getExchangeRate("USD")).thenReturn(fromRate);
        when(nbrbClient.getExchangeRate("EUR")).thenReturn(toRate);

        BigDecimal result = service.convert(amount, WalletCurrency.USD, WalletCurrency.EUR);

        assertEquals(new BigDecimal("66.6667"), result);

        verify(nbrbClient).getExchangeRate("USD");
        verify(nbrbClient).getExchangeRate("EUR");
    }
}