package art.timestop.users.exception;

import java.time.LocalDateTime;

public record ExceptionResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {}
