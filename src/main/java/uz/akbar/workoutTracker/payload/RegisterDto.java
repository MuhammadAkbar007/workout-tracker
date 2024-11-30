package uz.akbar.workoutTracker.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** RegisterDto */
public record RegisterDto(
        @NotBlank(message = "Username is required")
                @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
                String username,
        @NotBlank(message = "Email is required") @Email(message = "Email should be valid")
                String email,
        @NotBlank(message = "Password is required")
                @Size(min = 5, message = "Password must be at least 5 characters long")
                String password) {}
