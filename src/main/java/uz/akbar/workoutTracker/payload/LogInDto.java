package uz.akbar.workoutTracker.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** LogInDto */
public record LogInDto(
        @NotBlank(message = "Username is required")
                @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
                String username,
        @NotBlank(message = "Password is required")
                @Size(min = 5, message = "Password must be at least 5 characters long")
                String password) {}
