package com.solutions.apex.fin_manager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WalletExchangeRequestDTO(
        @NotNull(message = "ID отправителя не может быть пустым")
        Long senderWalletId,

        @NotNull(message = "ID получателя не может быть пустым")
        Long receiverWalletId,

        @NotNull(message = "сумма перевода не может быть пустой")
        @Positive(message = "сумма перевода должна быть положительной")
        BigDecimal amount
) {
}
