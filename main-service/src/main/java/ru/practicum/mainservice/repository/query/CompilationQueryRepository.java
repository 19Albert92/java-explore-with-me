package ru.practicum.mainservice.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.QCompilation;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompilationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Compilation> findCompilationByPinedFilter(Boolean pinned, int from, int size) {

        QCompilation qCompilation = QCompilation.compilation;

        BooleanBuilder builder = new BooleanBuilder();

        if (pinned != null) {
            builder.and(qCompilation.pinned.eq(pinned));
        }

        return jpaQueryFactory.selectFrom(qCompilation)
                .where(builder)
                .offset(from)
                .limit(size)
                .fetch();
    }
}
