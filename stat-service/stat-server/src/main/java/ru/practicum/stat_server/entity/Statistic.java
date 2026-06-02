package ru.practicum.stat_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;

    private String uri;

    private String ip;

    @Column(name = "hit_timestamp")
    private LocalDateTime timestamp;
}
