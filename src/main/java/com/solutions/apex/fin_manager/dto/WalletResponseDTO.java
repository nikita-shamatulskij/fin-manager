package com.solutions.apex.fin_manager.dto;

import com.solutions.apex.fin_manager.model.WalletCurrency;

import java.math.BigDecimal;

public record WalletResponseDTO(
        Long id,
        WalletCurrency currency,
        BigDecimal balance
) {
}
