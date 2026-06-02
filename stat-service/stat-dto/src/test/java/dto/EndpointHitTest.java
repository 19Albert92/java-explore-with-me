package dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import shared.dto.EndpointHit;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointHitTest {

    private Validator validator;

    private final EndpointHit endpointHit = new EndpointHit();

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        endpointHit.setIp("127.0.0.1");
        endpointHit.setApp("app1");
        endpointHit.setTimestamp("2022-09-06 11:00:23");
        endpointHit.setUri("/events/1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void validateField_app(String app) {

        String expectedErrorMessage = "Идентификатор сервиса не должен быть пустым";

        endpointHit.setApp(app);
        assertThat(validator.validate(endpointHit))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void validateField_uri(String uri) {

        String expectedErrorMessage = "URI не должен быть пустым";

        endpointHit.setUri(uri);
        assertThat(validator.validate(endpointHit))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void validateField_ip(String ip) {

        String expectedErrorMessage = "IP-адрес пользователя не должен быть пустым";

        endpointHit.setIp(ip);
        assertThat(validator.validate(endpointHit))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void validateField_timestamp(String timestamp) {

        String expectedErrorMessage = "Дата и время не должен быть пустым";

        endpointHit.setTimestamp(timestamp);
        assertThat(validator.validate(endpointHit))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }
}