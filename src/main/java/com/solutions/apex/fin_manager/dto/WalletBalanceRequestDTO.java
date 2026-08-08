package com.solutions.apex.fin_manager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WalletBalanceRequestDTO(
        @NotNull(message = "Сумма обязательна")
        @Positive(message = "Сумма должна быть больше нуля")
        BigDecimal amount
) {
}
