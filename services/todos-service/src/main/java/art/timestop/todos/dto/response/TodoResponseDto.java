package art.timestop.todos.dto.response;

public record TodoResponseDto(
    long id,
    String title,
    String description,
    int priority,
    boolean completed,
    Long ownerId
) {}
