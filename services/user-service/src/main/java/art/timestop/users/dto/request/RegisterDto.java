package art.timestop.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterDto(

    @NotEmpty(message = "First name can't be empty")
    @Size(min = 3, max = 30, message = "First name must be between 3-30 characters")
    String firstName,

    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 3, max = 30, message = "Last name must be between 3-30 characters")
    String lastName,

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Must be valid email")
    @Size(max = 100, message = "Email can't be more than 100 characters")
    String email,

    @NotEmpty(message = "Password can't be empty")
    @Size(min = 5, message = "Password must be at least 5 characters")
    String password

) {}
