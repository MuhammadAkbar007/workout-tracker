package uz.akbar.workoutTracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uz.akbar.workoutTracker.entity.WorkoutPlan;
import uz.akbar.workoutTracker.enums.WorkoutStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/** WorkoutPlanRepository */
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    Page<WorkoutPlan> findByOwnerId(UUID userId, Pageable pageable);

    Page<WorkoutPlan> findAllByStatus(WorkoutStatus status, Pageable pageable);

    Page<WorkoutPlan> findAllByStatusAndOwnerId(
            WorkoutStatus status, UUID ownerId, Pageable pageable);

    Optional<WorkoutPlan> findByIdAndOwnerId(UUID id, UUID ownerId);

    Optional<WorkoutPlan> findByOwnerIdAndScheduledDateTime(
            UUID ownerId, Instant scheduledDateTime);

    long countByStatusAndOwnerId(WorkoutStatus status, UUID ownerId);

    @Query("SELECT MIN(wp.scheduledDateTime) FROM WorkoutPlan wp WHERE wp.owner.id = :ownerId")
    Optional<Instant> findOldestScheduledDateTimeByOwnerId(@Param("ownerId") UUID ownerId);

    @Modifying
    int deleteByIdAndOwnerId(UUID id, UUID ownerId);
}
