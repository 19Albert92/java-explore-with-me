package ru.practicum.mainservice.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.QComment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Comment> findAllByFilter(Long eventId, CommentFilterParams filter) {

        QComment qComment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qComment.event.id.eq(eventId));

        if (filter.getUserIds() != null) {
            builder.and(qComment.user.id.in(filter.getUserIds()));
        }

        if (filter.getRangeStart() != null) {
            builder.and(qComment.createdAt.goe(filter.getRangeStart()));
        }

        if (filter.getRangeEnd() != null) {
            builder.and(qComment.createdAt.loe(filter.getRangeEnd()));
        }

        return jpaQueryFactory
                .selectFrom(qComment)
                .where(builder)
                .offset(filter.getFrom())
                .limit(filter.getSize())
                .orderBy(qComment.createdAt.asc())
                .fetch();
    }
}
