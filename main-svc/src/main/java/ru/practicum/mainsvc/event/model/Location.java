package ru.practicum.mainsvc.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Location {
    @Column(name = "location_lat")
    private Double lat;
    @Column(name = "location_lon")
    private Double lon;
}
