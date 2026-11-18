package art.timestop.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Must be valid email")
    String email,

    @NotEmpty(message = "Password can't be empty")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters")
    String password

) {}
