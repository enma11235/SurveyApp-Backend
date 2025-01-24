package com.github.enma11235.generic.poll.system.repository;
import com.github.enma11235.generic.poll.system.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
    // No es necesario definir ni implementar métodos. Spring Data JPA lo hace automáticamente.
}