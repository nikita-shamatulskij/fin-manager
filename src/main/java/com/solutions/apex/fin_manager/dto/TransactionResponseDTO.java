package com.solutions.apex.fin_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        Long senderWalletId,
        Long receiverWalletId,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
