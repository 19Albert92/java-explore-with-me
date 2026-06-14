package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.entity.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
