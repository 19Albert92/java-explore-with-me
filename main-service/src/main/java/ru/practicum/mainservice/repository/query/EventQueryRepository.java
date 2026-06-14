package ru.practicum.mainservice.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.dto.event.FilterEventDto;
import ru.practicum.mainservice.dto.event.QueryEventsDto;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventSortView;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.event.QEvent;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.QEventRequest;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression categoryIn(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }

        return QEvent.event.category.id.in(categoryIds);
    }

    private BooleanExpression dateRangeGoe(LocalDateTime rangeStart) {
        LocalDateTime start = (rangeStart != null) ? rangeStart : LocalDateTime.now();
        return QEvent.event.eventDate.goe(start);
    }

    private BooleanExpression dateRangeLoe(LocalDateTime rangeEnd) {
        if (rangeEnd == null) {
            return null;
        }
        return QEvent.event.eventDate.loe(rangeEnd);
    }

    private BooleanExpression stateIn(List<EventState> states) {
        if (states == null || states.isEmpty()) {
            return null;
        }

        return QEvent.event.state.in(states);
    }

    public List<Event> findEventsByFilter(FilterEventDto filterEventDto) {

        QEvent qEvent = QEvent.event;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(stateIn(List.of(EventState.PUBLISHED)));

        if (filterEventDto.text() != null && !filterEventDto.text().isEmpty()) {
            String searchText = filterEventDto.text().trim();

            builder.and(
                    qEvent.annotation.containsIgnoreCase(searchText)
                            .or(qEvent.description.containsIgnoreCase(searchText))
            );
        }

        builder.and(categoryIn(filterEventDto.categories()));

        if (filterEventDto.paid() != null) {
            builder.and(qEvent.paid.eq(filterEventDto.paid()));
        }

        builder.and(dateRangeGoe(filterEventDto.rangeStart()));
        builder.and(dateRangeLoe(filterEventDto.rangeEnd()));

        if (filterEventDto.onlyAvailable() != null  && filterEventDto.onlyAvailable()) {

            QEventRequest qEventRequest = QEventRequest.eventRequest;

            builder.and(
                    qEvent.participantLimit.eq(0)
                            .or(
                                    qEvent.participantLimit.gt(
                                            JPAExpressions.select(qEventRequest.id.count())
                                                    .from(qEventRequest)
                                                    .where(qEventRequest.event.id.eq(qEvent.id)
                                                            .and(qEventRequest.status.eq(ApplicationStatus.CONFIRMED)))
                                    )
                            )
            );
        }

        JPAQuery<Event> query = jpaQueryFactory
                .selectFrom(qEvent)
                .where(builder);

        if (filterEventDto.sort() == EventSortView.EVENT_DATE) {
            query.orderBy(qEvent.eventDate.asc());
        }

        return query
                .offset(filterEventDto.from() == null ? 0 : filterEventDto.from())
                .limit(filterEventDto.size() == null ? 10 : filterEventDto.size())
                .fetch();
    }

    public List<Event> queryEventsByAdminFilter(QueryEventsDto queryDto) {

        QEvent qEvent = QEvent.event;

        BooleanBuilder builder = new BooleanBuilder();

        if (queryDto.users() != null && !queryDto.users().isEmpty()) {

            builder.and(qEvent.initiator.id.in(queryDto.users()));
        }

        builder.and(stateIn(queryDto.states()));

        builder.and(categoryIn(queryDto.categories()));

        builder.and(dateRangeGoe(queryDto.rangeStart()));

        builder.and(dateRangeLoe(queryDto.rangeEnd()));

        return jpaQueryFactory
                .selectFrom(qEvent)
                .where(builder)
                .offset(queryDto.from() == null ? 0 : queryDto.from())
                .limit(queryDto.size() == null ? 10 : queryDto.size())
                .fetch();
    }
}
