package art.timestop.todos.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import art.timestop.todos.service.TodoService;

@Component
public class UserEventConsumer {

    private final TodoService todoService;

    public UserEventConsumer(TodoService todoService){
		this.todoService = todoService;}

    @KafkaListener (
        topics = "user.deleted",
        groupId = "todos-service"
    )
    public void handleUserDeleted(String ownerId) {
        System.out.println("KAFKA EVENT RECEIVED: " + ownerId);
        todoService.deleteTodosByOwner(ownerId);
        System.out.println("Todos deleted for user " + ownerId);
    }

}
