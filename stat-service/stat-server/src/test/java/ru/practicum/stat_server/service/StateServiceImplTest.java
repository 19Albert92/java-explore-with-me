package ru.practicum.stat_server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stat_server.exception.DateInvalidateException;
import ru.practicum.stat_server.repository.StateRepository;
import ru.practicum.stat_server.service.impl.StateServiceImpl;
import shared.UtilConstant;
import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StateServiceImplTest {

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateServiceImpl stateService;

    @Test
    void shouldReturnVoid_whenSaveStatisticSuccessfully() {

        EndpointHit endpoint = new EndpointHit(
                "app1", "/events/1", "127.0.0.1", "2022-09-06 11:00:23");

        stateService.saveHit(endpoint);

        Mockito.verify(stateRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldReturnListViewStats_whenGetStatisticByFilter() {

        String[] events = {"/events/1"};

        ViewStats viewStats = new ViewStats();
        viewStats.setApp("app1");
        viewStats.setHits(1L);
        viewStats.setUri("/events/1");

        Mockito.when(stateRepository.findUniqStatsWithUris(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(viewStats));

        RequestFilterState filter = RequestFilterState.builder()
                .start(LocalDateTime.parse("2022-09-06 11:00:23", UtilConstant.formatter))
                .end(LocalDateTime.parse("2024-05-06 09:00:13", UtilConstant.formatter))
                .uris(events)
                .unique(true)
                .build();

        List<ViewStats> result = stateService.getStateByFilter(filter);

        ViewStats firstStat = result.getFirst();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("app1", firstStat.getApp());
        Assertions.assertEquals("/events/1", firstStat.getUri());
        Assertions.assertEquals(1L, firstStat.getHits());

        Mockito.verify(stateRepository, Mockito.times(1))
                .findUniqStatsWithUris(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldReturnDateInvalidateException_whenStartDateAfterEndDate() {

        RequestFilterState filter = RequestFilterState.builder()
                .start(LocalDateTime.parse("2024-09-06 11:00:23", UtilConstant.formatter))
                .end(LocalDateTime.parse("2024-05-06 09:00:13", UtilConstant.formatter))
//                .uris(List.of("/events/1"))
                .unique(true)
                .build();

        Assertions.assertThrows(DateInvalidateException.class, () -> stateService.getStateByFilter(filter));

        Mockito.verifyNoInteractions(stateRepository);
    }
}