package uz.akbar.workoutTracker.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;

import uz.akbar.workoutTracker.enums.WorkoutStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/** WorkoutPlanUpdateDto */
public record WorkoutPlanUpdateDto(
        WorkoutStatus status,
        @JsonFormat(
                        shape = JsonFormat.Shape.STRING,
                        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                        timezone = "UTC") // 2024-12-30T14:30:00.000+0500
                // sample Instant with Uzb timezone
                @FutureOrPresent(message = "Schedule must be in the future")
                Instant scheduledDateTime,
        UUID ownerId,
        Set<UUID> exerciseIds) {}
