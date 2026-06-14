package ru.practicum.mainservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record NewUserRequest(

        @NotBlank(message = "Field: email. Error: must not be blank. Value: null")
        @Email(message = "Email не валиден")
        @Length(min = 6, max = 254, message = "Email должен состоять из 6 - 254 символов")
        String email,

        @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
        @Length(min = 2, max = 250, message = "Имя должен состоять из 6 - 254 символов")
        String name
) {
}