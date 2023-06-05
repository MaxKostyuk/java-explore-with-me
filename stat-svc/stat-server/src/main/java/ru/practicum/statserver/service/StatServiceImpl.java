package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.statserver.mapper.StatMapper;
import ru.practicum.statserver.repository.StatRecordRepository;
import ru.practicum.statdto.StatHitDto;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRecordRepository repository;

    @Override
    public void saveStat(StatHitDto statHit) {
        repository.save(StatMapper.toStatRecord(statHit));
    }

    @Override
    public List<StatViewDto> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Sort sort = Sort.by("hits").descending();
        if (Objects.isNull(uris)) {
            if (unique)
                return repository.statForAllUriUniqueIp(start, end, sort);
            else
                return repository.statForAllUri(start, end, sort);
        } else {
            if (unique)
                return repository.statForSpecificUrisUniqueIp(start, end, uris, sort);
            else
                return repository.statForSpecificUris(start, end, uris, sort);
        }
    }
}
