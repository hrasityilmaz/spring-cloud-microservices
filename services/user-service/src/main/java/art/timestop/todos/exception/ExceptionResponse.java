package art.timestop.todos.exception;

import java.time.LocalDateTime;

public record ExceptionResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {}