package art.timestop.todos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import art.timestop.todos.entity.Todo;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {
    List<Todo> findByOwner(String ownerId);
    Optional<Todo> findByIdAndOwner(Long id, String ownerId);
    void deleteByOwner(String ownerId);
    void deleteTodosByOwner(String ownerId);
}
