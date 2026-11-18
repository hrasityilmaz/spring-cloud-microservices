package art.timestop.todos.service;

import org.springframework.stereotype.Service;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResonseDto;
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
	public TodoResonseDto createTodo(TodoRequestDto request, String userId) {

        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setPriority(request.priority());
        todo.setCompleted(false);
        todo.setId(request.ownerId());

        Todo savedTodo = todoRepository.save(todo);

        TodoResonseDto todoResponse = new TodoResonseDto(
            savedTodo.getId(),
            savedTodo.getTitle(),
            savedTodo.getDescription(),
            savedTodo.getPriority(),
            savedTodo.isCompleted(),
            savedTodo.getId());

        return todoResponse;
	}




}
