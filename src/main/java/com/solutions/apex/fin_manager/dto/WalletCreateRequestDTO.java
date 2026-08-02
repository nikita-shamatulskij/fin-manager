package com.solutions.apex.fin_manager.dto;

import com.solutions.apex.fin_manager.model.WalletCurrency;
import jakarta.validation.constraints.NotNull;

public record WalletCreateRequestDTO(
        @NotNull(message = "ID пользователя обязательно")
        Long userId,

        @NotNull(message = "Валюта обязательна")
        WalletCurrency currency
) {
}
