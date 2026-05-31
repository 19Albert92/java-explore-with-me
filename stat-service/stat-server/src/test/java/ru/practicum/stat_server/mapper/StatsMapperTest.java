package ru.practicum.stat_server.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.stat_server.entity.Statistic;
import shared.UtilConstant;
import shared.dto.EndpointHit;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class StatsMapperTest {

    @Test
    void mapToStatistic() {

        String expectedUri = "/events/1";
        String expectedApp = "ewm-main-service";
        String expectedIp = "192.163.0.1";
        String expectedTimestamp = "2022-09-06 11:00:23";

        EndpointHit endpoint = new EndpointHit();

        endpoint.setUri(expectedUri);
        endpoint.setApp(expectedApp);
        endpoint.setIp(expectedIp);
        endpoint.setTimestamp(expectedTimestamp);

        Statistic statistic = StatsMapper.mapToStatistic(endpoint);

        assertThat(statistic)
                .as("Статистика не должна быть null")
                .isNotNull()
                .hasFieldOrPropertyWithValue("uri", expectedUri)
                .hasFieldOrPropertyWithValue("app", expectedApp)
                .hasFieldOrPropertyWithValue("ip", expectedIp)
                .hasFieldOrPropertyWithValue("timestamp",
                        LocalDateTime.parse(expectedTimestamp, UtilConstant.formatter));
    }
}