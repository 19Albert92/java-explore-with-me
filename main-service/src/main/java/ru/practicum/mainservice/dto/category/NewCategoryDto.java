package ru.practicum.mainservice.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record NewCategoryDto(
        @NotNull(message = "Field: name. Error: must not be null")
        @NotBlank(message = "Field: name. Error: must not be blank. Value: ''")
        @Length(min = 1, max = 50, message = "Название категории должно иметь от 1 до 50 символов")
        String name
) {
}
