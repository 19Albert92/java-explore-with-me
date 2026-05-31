package ru.practicum.stat_server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stat_server.entity.Statistic;
import shared.UtilConstant;
import shared.dto.EndpointHit;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.NONE)
public class StatsMapper {

    public static Statistic mapToStatistic(EndpointHit endpointHit) {
        return Statistic.builder()
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), UtilConstant.formatter))
                .build();
    }
}
