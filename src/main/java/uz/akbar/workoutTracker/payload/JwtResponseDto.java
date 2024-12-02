package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import java.time.Instant;

/** AuthResponseDto */
@Builder
public record JwtResponseDto(
        String username,
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiryTime,
        Instant refreshTokenExpiryTime) {}
