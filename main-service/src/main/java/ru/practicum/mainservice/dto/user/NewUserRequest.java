package ru.practicum.mainservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record NewUserRequest(

        @NotNull(message = "Поле email обязательно к заполнению")
        @Email(message = "Email не валиден")
        @Length(min = 6, max = 254, message = "Email должен состоять из 6 - 254 символов")
        String email,

        @NotNull(message = "Поле name обязательно к заполнению")
        @Length(min = 2, max = 250, message = "Имя должен состоять из 6 - 254 символов")
        String name
) {
}