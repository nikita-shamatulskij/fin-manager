package com.solutions.apex.fin_manager.mapper;

import com.solutions.apex.fin_manager.dto.UserRegistrationRequestDTO;
import com.solutions.apex.fin_manager.dto.UserResponseDTO;
import com.solutions.apex.fin_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(UserRegistrationRequestDTO dto);

    UserResponseDTO toDTO(User user);
}
