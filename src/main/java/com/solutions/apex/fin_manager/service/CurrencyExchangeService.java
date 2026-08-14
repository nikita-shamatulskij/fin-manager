package com.solutions.apex.fin_manager.service;

import com.solutions.apex.fin_manager.client.NbrbClient;
import com.solutions.apex.fin_manager.dto.NbrbRateResponseDTO;
import com.solutions.apex.fin_manager.model.WalletCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {

    private final NbrbClient nbrbClient;

    public BigDecimal convert(BigDecimal amount, WalletCurrency currencyFrom, WalletCurrency currencyTo){
        if (currencyFrom.equals(currencyTo)){
            return amount;
        }

        if (currencyFrom.equals(WalletCurrency.BYN)){
            var rate = nbrbClient.getExchangeRate(currencyTo.name());

            return amount
                    .multiply(BigDecimal.valueOf(rate.Cur_Scale()))
                    .divide(rate.Cur_OfficialRate(), 4, RoundingMode.HALF_UP);
        }

        else if (currencyTo.equals(WalletCurrency.BYN)){
            var rate = nbrbClient.getExchangeRate(currencyFrom.name());

            return amount
                    .multiply(rate.Cur_OfficialRate())
                    .divide(BigDecimal.valueOf(rate.Cur_Scale()), 4, RoundingMode.HALF_UP);
        }

        else {
            NbrbRateResponseDTO fromRate = nbrbClient.getExchangeRate(currencyFrom.name());
            NbrbRateResponseDTO toRate = nbrbClient.getExchangeRate(currencyTo.name());

            BigDecimal inByn = amount
                    .multiply(fromRate.Cur_OfficialRate())
                    .divide(BigDecimal.valueOf(fromRate.Cur_Scale()), 4, RoundingMode.HALF_UP);

            return inByn
                    .multiply(BigDecimal.valueOf(toRate.Cur_Scale()))
                    .divide(toRate.Cur_OfficialRate(), 4, RoundingMode.HALF_UP);
        }
    }
}