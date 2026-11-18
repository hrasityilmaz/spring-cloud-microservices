package art.timestop.todos.service;

import org.springframework.stereotype.Service;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResonseDto;
import art.timestop.todos.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
    }

	@Override
	public TodoResonseDto createTodo(TodoRequestDto request) {
		return null;
	}


}
