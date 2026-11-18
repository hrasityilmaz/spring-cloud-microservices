package art.timestop.todos.service;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResonseDto;

public interface TodoService  {
    TodoResonseDto createTodo(TodoRequestDto request);
}
