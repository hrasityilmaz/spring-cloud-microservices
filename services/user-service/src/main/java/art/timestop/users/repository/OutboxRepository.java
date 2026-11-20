package art.timestop.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import art.timestop.users.entities.OutboxEvent;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long>{
    List<OutboxEvent> findTop100ByStatusOrderByIdAsc(
            String status
        );
}
