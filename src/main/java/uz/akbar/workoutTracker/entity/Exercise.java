package uz.akbar.workoutTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

import uz.akbar.workoutTracker.enums.ExerciseCategory;
import uz.akbar.workoutTracker.enums.MuscleGroup;

import java.util.UUID;

/** Exercise */
@Entity
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private int sets;

    private int repetitions;

    private double weight;

    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;
}
