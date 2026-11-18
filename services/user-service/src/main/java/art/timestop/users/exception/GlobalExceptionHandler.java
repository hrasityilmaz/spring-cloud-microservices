
package art.timestop.users.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(
            IllegalArgumentException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    // Surprise errors coming 500 !
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntime(
            RuntimeException ex) {
        return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(Exception ex, HttpStatus status) {
        ExceptionResponse error = new ExceptionResponse(
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }
}
