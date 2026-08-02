package com.solutions.apex.fin_manager.mapper;

import com.solutions.apex.fin_manager.dto.WalletResponseDTO;
import com.solutions.apex.fin_manager.model.Wallet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponseDTO entityToDto(Wallet wallet);
    List<WalletResponseDTO> entityToDto(List<Wallet> wallets);

}
