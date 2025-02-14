package com.quickpolls.repository;
import com.quickpolls.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
    // No es necesario definir ni implementar métodos. Spring Data JPA lo hace automáticamente.
}