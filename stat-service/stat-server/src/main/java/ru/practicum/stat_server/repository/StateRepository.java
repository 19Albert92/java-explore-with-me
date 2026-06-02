package ru.practicum.stat_server.repository;

import shared.dto.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.stat_server.entity.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<Statistic, Long> {

    @Query("""
        SELECT new shared.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip))
        FROM Statistic AS s
        WHERE s.timestamp >= :start AND s.timestamp <= :end
        AND s.uri IN :uris
        GROUP BY s.app, s.uri
        ORDER BY COUNT(DISTINCT s.ip) DESC
        """)
    List<ViewStats> findUniqStatsWithUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("""
        SELECT new shared.dto.ViewStats(s.app, s.uri, COUNT(s.ip))
        FROM Statistic AS s
        WHERE s.timestamp >= :start AND s.timestamp <= :end
        AND s.uri IN :uris
        GROUP BY s.app, s.uri
        ORDER BY COUNT(s.ip) DESC
        """)
    List<ViewStats> findNotUniqStatsWithUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("""
        SELECT new shared.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip))
        FROM Statistic AS s
        WHERE s.timestamp >= :start AND s.timestamp <= :end
        GROUP BY s.app, s.uri
        ORDER BY COUNT(DISTINCT s.ip) DESC
        """)
    List<ViewStats> findUniqStatsAllUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT new shared.dto.ViewStats(s.app, s.uri, COUNT(s.ip))
        FROM Statistic AS s
        WHERE s.timestamp >= :start AND s.timestamp <= :end
        GROUP BY s.app, s.uri
        ORDER BY COUNT(s.ip) DESC
        """)
    List<ViewStats> findNotUniqStatsAllUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
