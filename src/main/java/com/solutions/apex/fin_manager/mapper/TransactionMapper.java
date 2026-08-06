package com.solutions.apex.fin_manager.mapper;

import com.solutions.apex.fin_manager.dto.TransactionCreateRequestDTO;
import com.solutions.apex.fin_manager.dto.TransactionResponseDTO;
import com.solutions.apex.fin_manager.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "senderWalletId", source = "senderWallet.id")
    @Mapping(target = "receiverWalletId", source = "receiverWallet.id")
    TransactionResponseDTO toDTO(Transaction entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senderWallet", ignore = true)
    @Mapping(target = "receiverWallet", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transaction toEntity(TransactionCreateRequestDTO dto);

}