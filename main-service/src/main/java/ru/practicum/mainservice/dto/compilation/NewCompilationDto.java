package ru.practicum.mainservice.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Builder
public record NewCompilationDto(
        Set<Integer> events,
        boolean pinned,
        @NotBlank(message = "Field: title. Error: must not be blank. Value: {validatedValue}")
        @Length(min = 1, max = 50, message = "Заголовок должен содержать не больше 50 символов")
        String title
) {
}
