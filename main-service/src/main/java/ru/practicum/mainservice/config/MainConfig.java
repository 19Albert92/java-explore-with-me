package ru.practicum.mainservice.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClientService;

@Configuration
public class MainConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public StatClientService statClientService(@Value("${stat.service.url}") String url) {
        return new StatClientService(url);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
