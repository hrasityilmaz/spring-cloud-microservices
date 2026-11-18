package art.timestop.todos.dto.response;

public record TodoResonseDto(
    long id,
    String title,
    String description,
    int priority,
    boolean completed
) {}
