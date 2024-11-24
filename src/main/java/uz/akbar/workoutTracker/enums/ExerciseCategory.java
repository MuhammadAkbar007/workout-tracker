package uz.akbar.workoutTracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** ExerciseCategory */
@AllArgsConstructor
@Getter
public enum ExerciseCategory {
    STRENGTH("Strength training"),
    CARDIO("Cardiovascular"),
    FLEXIBILITY("Flexibility"),
    HIIT("High Intensity Interval Training"),
    BALANCE("Balance and Stability");

    private final String description;
}
