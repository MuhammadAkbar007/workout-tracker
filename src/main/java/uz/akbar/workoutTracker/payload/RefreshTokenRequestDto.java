package uz.akbar.workoutTracker.payload;

import jakarta.validation.constraints.NotBlank;

/** RefreshTokenRequestDto */
public record RefreshTokenRequestDto(
        @NotBlank(message = "token is required") String refreshToken) {}
