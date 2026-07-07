package ru.practicum.mainservice.dto.comment;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

import static shared.UtilConstant.DATE_TIME_FORMAT;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentFilterParams {
    @PositiveOrZero
    @Builder.Default
    int from = 0;
    @Positive
    @Builder.Default
    int size = 10;
    Set<Long> userIds;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeEnd;
}
