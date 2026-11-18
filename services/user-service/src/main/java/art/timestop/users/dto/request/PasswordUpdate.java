package art.timestop.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdate(
    @NotBlank (message = "password cant be empty")
    @Size (min = 5, max = 30, message = "Password must between 5-30 characters")
    String oldPassword,
    @NotBlank (message = "password cant be empty")
    @Size (min = 5, max = 30, message = "Password must between 5-30 characters")
    String newPassword,
    @NotBlank (message = "password cant be empty")
    @Size (min = 5, max = 30, message = "Password must between 5-30 characters")
    String repeatPassword
) {}
