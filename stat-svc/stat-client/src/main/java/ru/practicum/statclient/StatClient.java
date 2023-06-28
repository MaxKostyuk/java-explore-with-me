package ru.practicum.statclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statdto.StatHitDto;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient {

    private static final String SAVE_ENDPOINT_ADRESS = "/hit";
    private static final String GET_ENDPOINT_ADRESS = "/stats";

    private final RestTemplate rest;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
    }

    public int saveEndpointRequest(StatHitDto statsRequestDto) {
        HttpEntity<StatHitDto> requestEntity = new HttpEntity<>(statsRequestDto, defaultHeaders());
        return rest.exchange(SAVE_ENDPOINT_ADRESS, HttpMethod.POST, requestEntity, Object.class).getStatusCodeValue();
    }

    public List<StatViewDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", String.join(",", uris),
                "unique", unique
        );
        String path = GET_ENDPOINT_ADRESS + "?start={start}&end={end}&uris={uris}&unique={unique}";
        HttpEntity<Object> requestEntity = new HttpEntity<>(defaultHeaders());
        return rest.exchange(path, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<StatViewDto>>() {}, parameters).getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
