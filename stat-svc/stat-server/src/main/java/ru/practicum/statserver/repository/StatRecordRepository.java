package ru.practicum.statserver.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statserver.model.StatRecord;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRecordRepository extends JpaRepository<StatRecord, Long> {

    @Query("select new ru.practicum.statdto.StatViewDto(st.app, st.uri, count(distinct st.ip) as hits)" +
            "from StatRecord st " +
            "where st.created between ?1 and ?2 " +
            "group by st.app, st.uri")
    List<StatViewDto> statForAllUriUniqueIp(LocalDateTime start, LocalDateTime end, Sort sort);

    @Query("select new ru.practicum.statdto.StatViewDto(st.app, st.uri, count(st.ip) as hits)" +
            "from StatRecord st " +
            "where st.created between ?1 and ?2 " +
            "group by st.app, st.uri")
    List<StatViewDto> statForAllUri(LocalDateTime start, LocalDateTime end, Sort sort);

    @Query("select new ru.practicum.statdto.StatViewDto(st.app, st.uri, count(distinct st.ip) as hits)" +
            "from StatRecord st " +
            "where st.created between ?1 and ?2 " +
            "and st.uri in ?3 " +
            "group by st.app, st.uri")
    List<StatViewDto> statForSpecificUrisUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort);

    @Query("select new ru.practicum.statdto.StatViewDto(st.app, st.uri, count(st.ip) as hits)" +
            "from StatRecord st " +
            "where st.created between ?1 and ?2 " +
            "and st.uri in ?3 " +
            "group by st.app, st.uri")
    List<StatViewDto> statForSpecificUris(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort);
}
