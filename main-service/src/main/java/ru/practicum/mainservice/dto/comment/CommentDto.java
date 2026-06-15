package ru.practicum.mainservice.dto.comment;

import lombok.Builder;

@Builder
public record CommentDto(
        Long id,
        String text,
        Long authorId,
        Long eventId,
        String createdAt
) {
}
