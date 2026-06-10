package ru.practicum.mainservice.entity.event;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Embeddable
public class Location {
    private float lat;
    private float lon;
}
