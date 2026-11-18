package art.timestop.todos.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import art.timestop.todos.entity.Todo;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {}
