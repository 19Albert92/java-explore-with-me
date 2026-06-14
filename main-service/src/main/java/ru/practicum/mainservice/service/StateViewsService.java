package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.StatClientService;
import shared.dto.ViewStats;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static shared.UtilConstant.FORMATTER;

@Log4j2
@Service
@RequiredArgsConstructor
public class StateViewsService {

    private final StatClientService statClientService;

    private static final String NOW = LocalDateTime.now().plusHours(2).format(FORMATTER);

    public Map<String, Long> getStaticsByFilter(
            LocalDateTime start,
            List<String> uris,
            boolean unique
    ) {
        try {
            log.debug("Get stats by filter {}, {}, {}", start, uris, unique);

            return statClientService.getStats(
                    start.format(FORMATTER),
                    NOW,
                    uris.toArray(new String[0]),
                    unique
            ).stream().collect(Collectors.toMap(
                    ViewStats::getUri,
                    ViewStats::getHits,
                    (existing, current) -> existing
            ));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return Collections.emptyMap();
        }
    }

    public void saveState(String ip, String uri) {
        try {
            log.info("Saving state for ip: {}, uri: {}", ip, uri);
            statClientService.saveHit("ewm-main-service", uri, ip, NOW);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
