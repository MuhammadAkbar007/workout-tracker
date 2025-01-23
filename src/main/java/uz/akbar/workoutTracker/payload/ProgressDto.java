package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import java.time.Instant;

/** ProgressDto */
@Builder
public record ProgressDto(long numberOfActive, long numberOfPending, Instant oldestScheduled) {}
