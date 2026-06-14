package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Builder
public record UpdateCompilationDto(
        Set<Integer> events,
        Boolean pinned,
        @Length(min = 1, max = 50, message = "Заголовок должен содержать не больше 50 символов")
        String title
) {

        public boolean hasEvents() {
                return events != null;
        }

        public boolean hasPinned() {
                return pinned != null;
        }

        public boolean hasTitle() {
                return title != null;
        }
}
