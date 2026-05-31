package ru.practicum.stat_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat_server.entity.Statistic;
import ru.practicum.stat_server.exception.DateInvalidateException;
import ru.practicum.stat_server.mapper.StatsMapper;
import ru.practicum.stat_server.repository.StateRepository;
import ru.practicum.stat_server.service.StateService;
import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Override
    public List<ViewStats> getStateByFilter(RequestFilterState filter) {

        log.debug("Фильтр стратистики по параметрам {}", filter);

        if (filter.getStart().isAfter(filter.getEnd())) {
            throw new DateInvalidateException("Запуск фильтра должен быть до окончания");
        }

        if (filter.getUris() == null || filter.getUris().length == 0) {
            return findStates(filter.getStart(), filter.getEnd(), filter.getUnique());
        }

        return findStatesByUris(filter.getStart(), filter.getEnd(), filter.getUnique(), filter.getUris());
    }

    @Transactional
    @Override
    public void saveHit(EndpointHit endpointHit) {

        Statistic statistic = StatsMapper.mapToStatistic(endpointHit);

        stateRepository.save(statistic);
    }

    private List<ViewStats> findStatesByUris(LocalDateTime start, LocalDateTime end, boolean uniq, String[] uris) {
        if (uniq) {
            return stateRepository.findUniqStatsWithUris(start, end, List.of(uris));
        }

        return stateRepository.findNotUniqStatsWithUris(start, end, List.of(uris));
    }

    private List<ViewStats> findStates(LocalDateTime start, LocalDateTime end, boolean uniq) {
        if (uniq) {
            return stateRepository.findUniqStatsAllUris(start, end);
        }

        return stateRepository.findNotUniqStatsAllUris(start, end);
    }
}
