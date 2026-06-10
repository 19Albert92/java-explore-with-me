package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.request.EventParticipantCount;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.EventRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<EventRequest, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    List<EventRequest> findAllByUserId(Long userId);

    @Query("""
        SELECT new ru.practicum.mainservice.dto.request.EventParticipantCount(r.event.id, COUNT(r.id))
        FROM EventRequest AS r
        WHERE r.status = :status AND r.event.id IN :eventIds
        GROUP BY r.event.id
        """)
    List<EventParticipantCount> getParticipantsByEventIdsAndStatus(
            @Param("eventIds") List<Long> eventIds,
            @Param("status") ApplicationStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE EventRequest AS r SET r.status = :status WHERE r.event.id = :eventId AND id IN :requestIds")
    void updateRequestsStatus(
            @Param("requestIds") List<Integer> requestIds,
            @Param("status") ApplicationStatus status,
            @Param("eventId") Long eventId
    );

    @Query("UPDATE EventRequest AS r SET r.status = 'REJECTED' WHERE r.event.id = :eventId AND status = 'PENDING'")
    void resetQueriesOther(@Param("eventId") Long eventId);

    List<EventRequest> findAllByEventId(Long eventId);
}
