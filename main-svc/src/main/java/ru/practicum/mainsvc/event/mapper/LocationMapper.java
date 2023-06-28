package ru.practicum.mainsvc.event.mapper;

import ru.practicum.mainsvc.event.dto.LocationDto;
import ru.practicum.mainsvc.event.model.Location;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
