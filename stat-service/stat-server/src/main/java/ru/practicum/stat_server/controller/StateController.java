package ru.practicum.stat_server.controller;

import shared.dto.EndpointHit;
import shared.dto.RequestFilterState;
import shared.dto.ViewStats;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat_server.service.StateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @GetMapping("/stats")
    public List<ViewStats> getStates(@Valid @ModelAttribute RequestFilterState filterState) {
        return stateService.getStateByFilter(filterState);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        stateService.saveHit(endpointHit);
    }
}
