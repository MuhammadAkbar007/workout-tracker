package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import uz.akbar.workoutTracker.enums.ExerciseCategory;
import uz.akbar.workoutTracker.enums.MuscleGroup;

import java.util.UUID;

/** ExerciseResponseDto */
@Builder
public record ExerciseResponseDto(
        UUID id,
        String name,
        String description,
        ExerciseCategory category,
        MuscleGroup muscleGroup,
        int repetition,
        int set,
        double weight) {}
