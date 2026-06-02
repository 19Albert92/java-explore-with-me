package shared.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    @NotBlank(message = "Идентификатор сервиса не должен быть пустым")
    private String app;
    @NotBlank(message = "URI не должен быть пустым")
    private String uri;
    @NotBlank(message = "IP-адрес пользователя не должен быть пустым")
    private String ip;
    @NotBlank(message = "Дата и время не должен быть пустым")
    private String timestamp;
}
