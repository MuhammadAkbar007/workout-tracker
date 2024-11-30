package uz.akbar.workoutTracker.payload;

import java.time.Instant;

/** JwtDto */
public record JwtDto(String token, Instant expiryDate) {}
