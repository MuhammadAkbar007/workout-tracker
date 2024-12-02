package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import java.time.Instant;

/** JwtDto */
@Builder
public record JwtDto(String token, Instant expiryDate) {}
