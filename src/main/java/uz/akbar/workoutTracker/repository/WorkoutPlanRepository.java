package uz.akbar.workoutTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.akbar.workoutTracker.entity.WorkoutPlan;

import java.util.UUID;

/** WorkoutPlanRepository */
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {}
