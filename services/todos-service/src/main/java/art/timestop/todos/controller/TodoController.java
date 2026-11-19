package art.timestop.todos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResponseDto;
import art.timestop.todos.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping ("api/todos")
@Validated
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService){
		this.todoService = todoService;
    }

    @ResponseStatus ( HttpStatus.OK )
    @GetMapping
    public List<TodoResponseDto> getAllTodos(@AuthenticationPrincipal String ownerId){
        return todoService.getAllTodos(ownerId);
    }

    @ResponseStatus ( HttpStatus.CREATED )
    @PostMapping ("/create/todo")
    public TodoResponseDto createTodo(@Valid @RequestBody TodoRequestDto todoRequest, @AuthenticationPrincipal String userId){
        return todoService.createTodo(todoRequest, userId);
    }

    @PutMapping ("/{id}")
    @ResponseStatus ( HttpStatus.OK )
    public TodoResponseDto toggleTodoCompletetion(@PathVariable @Min (1) long id, @AuthenticationPrincipal String ownerId){
        return todoService.toggleTodoCompletation(id, ownerId);
    }

    @ResponseStatus ( HttpStatus.NO_CONTENT )
    @DeleteMapping ("/{id}")
    public void deleteTodo(@PathVariable @Min (1) long id, @AuthenticationPrincipal String ownerId){
        todoService.deleteTodo(id, ownerId);
    }
}
