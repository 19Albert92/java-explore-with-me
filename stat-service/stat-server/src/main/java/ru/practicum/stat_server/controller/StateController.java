package ru.practicum.stat_server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat_server.service.StateService;
import shared.UtilConstant;
import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @GetMapping("/stats")
    public List<ViewStats> getStates(
            @RequestParam
            @DateTimeFormat(pattern = UtilConstant.DATE_TIME_FORMAT)
            LocalDateTime start,
            @RequestParam
            @DateTimeFormat(pattern = UtilConstant.DATE_TIME_FORMAT)
            LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        RequestFilterState filterState = RequestFilterState.builder()
                .start(start)
                .end(end)
                .unique(unique)
                .uris(uris)
                .build();

        return stateService.getStateByFilter(filterState);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        stateService.saveHit(endpointHit);
    }
}
