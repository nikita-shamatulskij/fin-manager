package com.solutions.apex.fin_manager.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionCreateRequestDTO(
        @NotNull(message = "ID кошелька отправителя обязательно")
        Long senderWalletId,

        @NotNull(message = "ID кошелька получателя обязательно")
        Long receiverWalletId,

        @NotNull(message = "Сумма перевода обязательна")
        BigDecimal amount
) {
}
