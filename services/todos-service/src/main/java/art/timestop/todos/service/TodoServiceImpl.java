package art.timestop.todos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResponseDto;
import art.timestop.todos.entity.Todo;
import art.timestop.todos.repository.TodoRepository;

// TODO:
// when user removed remove all todos also !!

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
    }

	@Override
	public TodoResponseDto createTodo(TodoRequestDto request, String userId) {

    	if (userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not authenticated"
            );
        }

        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setPriority(request.priority());
        todo.setCompleted(false);
        todo.setOwner(Long.parseLong(userId));

        Todo savedTodo = todoRepository.save(todo);

        return convertTodotoResponse(savedTodo);
	}

	private TodoResponseDto convertTodotoResponse(Todo todo) {
	    return new TodoResponseDto(
					 todo.getId(),
    				 todo.getTitle(),
    				 todo.getDescription(),
    				 todo.getPriority(),
    				 todo.isCompleted(),
    				 todo.getOwner());
	}

	@Transactional (readOnly = true)
	@Override
	public List<TodoResponseDto> getAllTodos(String ownerId) {
         return todoRepository.findByOwner(ownerId)
        	.stream()
        	.map(this::convertTodotoResponse)
        	.toList();
        	}

    @Transactional
	@Override
	public TodoResponseDto toggleTodoCompletation(Long id, String ownerId) {
		Optional<Todo> todo = todoRepository.findByIdAndOwner(id, ownerId);
		if(todo.isEmpty()){
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
		}

		todo.get().setCompleted(!todo.get().isCompleted());
		Todo updatedTodo = todoRepository.save(todo.get());

		return convertTodotoResponse(updatedTodo);
	}

	@Override
	@Transactional
	public void deleteTodo(Long id, String ownerId) {

    Todo todo = todoRepository.findByIdAndOwner(id, ownerId)
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo not found"
            )
        );

    todoRepository.delete(todo);
	}

	@Override
	@Transactional
	public void deleteTodosByOwner(String ownerId) {
    todoRepository.deleteByOwner(ownerId);
	}




}
