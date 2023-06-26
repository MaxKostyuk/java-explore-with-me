package ru.practicum.mainsvc.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;
import ru.practicum.mainsvc.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {

    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getUsersRequests(@PathVariable @Positive Long userId) {
        log.info("RequestPrivateController - getUsersRequests - userId = {}", userId);
        return service.getUsersRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long userId,
                                              @RequestParam @Positive Long eventId) {
        log.info("RequestPrivateController - addRequest - userId = {}, eventId = {}", userId, eventId);
        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long requestId) {
        log.info("RequestPrivateController - cancelRequest - userId = {}, requestId = {}", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }
}
