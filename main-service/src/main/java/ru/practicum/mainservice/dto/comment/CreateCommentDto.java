package ru.practicum.mainservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateCommentDto(
        @NotBlank(message = "The text of the comment should not be rustic.")
        @Length(min = 3, max = 100, message = "The text of the comment should be between 3 and 100 characters")
        String text
) {
}
