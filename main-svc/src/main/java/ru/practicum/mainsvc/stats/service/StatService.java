package ru.practicum.mainsvc.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.StatHitDto;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackages = {"ru.practicum.statclient"})
@RequiredArgsConstructor
public class StatService {

    private static final String APP_NAME = "ewm-main-service";
    private static final String URI_BASE = "/event/";

    private static final LocalDateTime START_TIME = LocalDateTime.of(2020, 1, 1, 0, 0);

    private final StatClient client;

    public Long getViews(Long eventId) {
        String[] uris = new String[1];
        uris[0] = URI_BASE + eventId;
        List<StatViewDto> statViews = (List<StatViewDto>) client.getStats(START_TIME, LocalDateTime.now(), uris, false).getBody();
        return statViews.get(0).getHits();
    }

    public Map<Long, Long> getViews(List<Long> ids) {
        String[] uris = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            uris[i] = URI_BASE + ids.get(i).toString();
        }
        List<StatViewDto> statViews = (List<StatViewDto>) client.getStats(START_TIME, LocalDateTime.now(), uris, false).getBody();
        return statViews.stream().collect(Collectors.toMap(x -> Long.getLong(x.getUri().replace(URI_BASE, "")), StatViewDto::getHits));
    }

    public void saveViews(Long eventId, String ip) {
        String uri = URI_BASE + eventId;
        StatHitDto statHitDto = new StatHitDto(null, APP_NAME, uri, ip, LocalDateTime.now());
        client.saveEndpointRequest(statHitDto);
    }

    public void saveViews(List<Long> ids, String ip) {
        LocalDateTime time = LocalDateTime.now();
        for (Long id : ids) {
            String uri = URI_BASE + id;
            StatHitDto statHitDto = new StatHitDto(null, APP_NAME, uri, ip, time);
            client.saveEndpointRequest(statHitDto);
        }
    }
}