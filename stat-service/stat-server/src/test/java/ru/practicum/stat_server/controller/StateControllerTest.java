package ru.practicum.stat_server.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stat_server.service.StateService;
import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class StateControllerTest {

    @Mock
    private StateService stateService;

    @InjectMocks
    private StateController stateController;

    @Test
    void shouldReturnStatusOk_whenHitSavingSuccessfully() {

        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp("127.0.0.1");
        endpointHit.setTimestamp("2022-09-06 11:00:23");
        endpointHit.setApp("app");
        endpointHit.setUri("/state");

        stateController.saveHit(endpointHit);

        Mockito.verify(stateService).saveHit(endpointHit);
    }

    @Test
    void shouldReturnListViewStats_whenFilterMatches() {

        RequestFilterState requestFilterState = RequestFilterState.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        List<ViewStats> stats = List.of(
                ViewStats.builder().app("ewm-main-service").uri("/events/1").hits(1L).build(),
                ViewStats.builder().app("ewm-main-service").uri("/events/3").hits(3L).build()
        );

        Mockito.when(stateService.getStateByFilter(requestFilterState)).thenReturn(stats);

        List<ViewStats> result = stateController.getStates(requestFilterState);

        assertThat(result)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("app", "ewm-main-service")
                .hasFieldOrPropertyWithValue("uri", "/events/1")
                .hasFieldOrPropertyWithValue("hits", 1L);

        Mockito.verify(stateService).getStateByFilter(requestFilterState);
    }
}