package com.solutions.apex.fin_manager.client;

import com.solutions.apex.fin_manager.dto.NbrbRateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class NbrbClient {

    private final RestClient restClient = RestClient.create("https://api.nbrb.by/exrates/rates");

    public NbrbRateResponseDTO getExchangeRate (String currencyCode){
        NbrbRateResponseDTO[] responseDTOS = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("periodicity", 0)
                        .build())
                .retrieve()
                .body(NbrbRateResponseDTO[].class);

        return Arrays.stream(responseDTOS)
                .filter(nbrbRateResponseDTO -> nbrbRateResponseDTO
                        .Cur_Abbreviation()
                        .equalsIgnoreCase(currencyCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Валюта " + currencyCode + " не найдена в Нацбанке"));

    }
}