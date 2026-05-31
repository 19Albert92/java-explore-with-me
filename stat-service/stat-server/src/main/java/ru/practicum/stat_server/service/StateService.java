package ru.practicum.stat_server.service;

import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;

import java.util.List;

public interface StateService {
    List<ViewStats> getStateByFilter(RequestFilterState filterState);

    void saveHit(EndpointHit endpointHit);
}
