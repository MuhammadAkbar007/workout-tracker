package uz.akbar.workoutTracker.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/** WorkoutPlanDto */
public record WorkoutPlanDto(
        @JsonFormat(
                        shape = JsonFormat.Shape.STRING,
                        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                        timezone = "UTC")
                @NotNull(message = "Scheduled date and time are required")
                @FutureOrPresent(message = "Schedule must be in the future")
                Instant
                        scheduledDateTime, // 2024-12-30T14:30:00.000+0500 sample Instant with Uzb
                                           // timezone
        @NotNull(message = "Exercises should be selected") Set<UUID> exerciseIds) {}
