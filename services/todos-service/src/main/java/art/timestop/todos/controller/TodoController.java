package art.timestop.todos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import art.timestop.todos.dto.request.TodoRequestDto;
import art.timestop.todos.dto.response.TodoResonseDto;
import art.timestop.todos.service.TodoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping ("api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService){
		this.todoService = todoService;
    }

    @ResponseStatus ( HttpStatus.CREATED )
    @PostMapping ("/create/todo")
    public TodoResonseDto createTodo(@Valid @RequestBody TodoRequestDto todoRequest, @AuthenticationPrincipal String userId){
        return todoService.createTodo(todoRequest, userId);
    }
}
