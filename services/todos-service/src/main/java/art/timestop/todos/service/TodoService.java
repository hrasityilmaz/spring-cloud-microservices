package art.timestop.todos.service;

import java.util.List;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResponseDto;

public interface TodoService  {
    TodoResponseDto createTodo(TodoRequestDto request, String userId);
    List<TodoResponseDto> getAllTodos(String ownerId);
    TodoResponseDto toggleTodoCompletation(Long id, String ownerId);
    void deleteTodo(Long id, String ownerId);
    void deleteTodosByOwner(String ownerId);
}
