package com.solutions.apex.fin_manager.dto;

import java.math.BigDecimal;

public record NbrbRateResponseDTO(
        String Cur_Abbreviation,
        Integer Cur_Scale,
        BigDecimal Cur_OfficialRate
) {
}
