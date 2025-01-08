package uz.akbar.workoutTracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import uz.akbar.workoutTracker.entity.WorkoutPlan;

import java.util.Optional;
import java.util.UUID;

/** WorkoutPlanRepository */
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    Page<WorkoutPlan> findByOwnerId(UUID userId, Pageable pageable);

    Optional<WorkoutPlan> findByIdAndOwnerId(UUID id, UUID ownerId);

    @Modifying
    int deleteByIdAndOwnerId(UUID id, UUID ownerId);
}
