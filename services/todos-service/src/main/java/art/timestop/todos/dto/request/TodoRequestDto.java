package art.timestop.todos.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequestDto(
    @NotBlank (message = "Title is mendotary!")
    @Size (min = 3, max = 50, message = "Min max 3/50")
    String title,
    @NotBlank (message = "Description cant be empty!")
    @Size (min = 3, max = 500)
    String description,
    @Min (value = 1)
    @Max (value = 5)
    int priority,
    Long ownerId
) {}
