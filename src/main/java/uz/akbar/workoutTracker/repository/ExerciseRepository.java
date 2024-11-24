package uz.akbar.workoutTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.akbar.workoutTracker.entity.Exercise;

import java.util.UUID;

/** ExerciseRepository */
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {}
