package shared.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import shared.UtilConstant;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RequestFilterState {
    @NotNull(message = "Дата начало валидации обязательна")
    @DateTimeFormat(pattern = UtilConstant.DATE_TIME_FORMAT)
    private LocalDateTime start;
    @NotNull(message = "Дата окончание валидации обязательна")
    @DateTimeFormat(pattern = UtilConstant.DATE_TIME_FORMAT)
    private LocalDateTime end;
    private List<String> uris;
    private boolean unique;
}
