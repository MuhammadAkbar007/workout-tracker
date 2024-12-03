package uz.akbar.workoutTracker.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

import uz.akbar.workoutTracker.entity.Comment;
import uz.akbar.workoutTracker.enums.WorkoutStatus;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** WorkoutPlanResponseDto */
@JsonInclude(
        JsonInclude.Include
                .NON_EMPTY) // excludes null and empty collections/arrays while returning to
// client-side
@Builder
public record WorkoutPlanResponseDto(
        UUID id,
        WorkoutStatus status,
        Instant scheduledDateTime,
        UUID ownerId,
        Set<ExerciseResponseDto> exercises,
        List<Comment> comments,
        Instant createdAt) {}
