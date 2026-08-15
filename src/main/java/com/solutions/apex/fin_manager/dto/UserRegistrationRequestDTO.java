package com.solutions.apex.fin_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequestDTO(
        @NotBlank(message = "Имя пользователя не должно быть пустым")
        @Size(min = 6, max = 10, message = "Имя пользователя должно содержать от 6 до 10 символов")
        String username,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 7, message = "Пароль должен быть не менее 7 символов")
        String password
) {
}
