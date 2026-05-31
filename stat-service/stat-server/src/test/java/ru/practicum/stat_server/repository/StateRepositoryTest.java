package ru.practicum.stat_server.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.stat_server.entity.Statistic;
import shared.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static shared.UtilConstant.FORMATTER;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @BeforeEach
    void setUp() {
        List<Statistic> statistics = List.of(
                Statistic.builder()
                        .ip("127.0.0.1")
                        .app("ewm-main-service")
                        .uri("/events/1")
                        .timestamp(LocalDateTime.parse("2022-09-06 11:00:23", FORMATTER))
                        .build(),
                Statistic.builder()
                        .ip("127.0.0.2")
                        .app("ewm-main-service")
                        .uri("/events/2")
                        .timestamp(LocalDateTime.parse("2022-09-08 12:00:23", FORMATTER))
                        .build(),
                Statistic.builder()
                        .ip("127.0.0.1")
                        .app("ewm-main-service")
                        .uri("/events/2")
                        .timestamp(LocalDateTime.parse("2022-09-14 09:20:23", FORMATTER))
                        .build(),
                Statistic.builder()
                        .ip("127.0.0.5")
                        .app("ewm-main-service")
                        .uri("/events/3")
                        .timestamp(LocalDateTime.parse("2022-09-14 09:20:23", FORMATTER))
                        .build()
        );

        stateRepository.saveAll(statistics);
    }

    @Test
    void shouldReturnedListUniqStatistic_whenConstraintByFilter() {

        List<ViewStats> uniqStatsAllUris = stateRepository.findUniqStatsAllUris(
                LocalDateTime.parse("2022-09-06 09:20:23", FORMATTER),
                LocalDateTime.parse("2022-09-14 12:00:23", FORMATTER)
        );

        assertThat(uniqStatsAllUris)
                .hasSize(3)
                .first()
                .hasFieldOrPropertyWithValue("uri", "/events/2");

        List<ViewStats> uniqStatsWithUris = stateRepository.findUniqStatsWithUris(
                LocalDateTime.parse("2022-09-06 09:20:23", FORMATTER),
                LocalDateTime.parse("2022-09-14 12:00:23", FORMATTER),
                List.of("/events/2","/events/1")
        );

        assertThat(uniqStatsWithUris)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("uri", "/events/2");
    }

    @Test
    void shouldReturnedListNotUniqStatistic_whenConstraintByFilter() {

        List<ViewStats> notUniqStatsAllUris = stateRepository.findNotUniqStatsAllUris(
                LocalDateTime.parse("2022-09-06 09:20:23", FORMATTER),
                LocalDateTime.parse("2022-09-14 12:00:23", FORMATTER)
        );

        assertThat(notUniqStatsAllUris)
                .hasSize(3)
                .first()
                .hasFieldOrPropertyWithValue("uri", "/events/2");

        List<ViewStats> notUniqStatsWithUris = stateRepository.findNotUniqStatsWithUris(
                LocalDateTime.parse("2022-09-06 09:20:23", FORMATTER),
                LocalDateTime.parse("2022-09-14 12:00:23", FORMATTER),
                List.of("/events/2","/events/1")
        );

        assertThat(notUniqStatsWithUris)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("uri", "/events/2");
    }
}