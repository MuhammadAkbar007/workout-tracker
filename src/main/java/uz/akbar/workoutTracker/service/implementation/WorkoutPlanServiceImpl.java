package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.Exercise;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.entity.WorkoutPlan;
import uz.akbar.workoutTracker.enums.WorkoutStatus;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.ExerciseResponseDto;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;
import uz.akbar.workoutTracker.payload.WorkoutPlanResponseDto;
import uz.akbar.workoutTracker.repository.ExerciseRepository;
import uz.akbar.workoutTracker.repository.WorkoutPlanRepository;
import uz.akbar.workoutTracker.service.WorkoutPlanService;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** WorkoutPlanServiceImpl */
@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    @Autowired private WorkoutPlanRepository repository;
    @Autowired private ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public AppResponse create(WorkoutPlanDto dto, User user) {
        if (dto.scheduledDateTime().isBefore(Instant.now()))
            throw new AppBadException("Schedule is not for future");

        Set<Exercise> exercises = new HashSet<>();
        Set<ExerciseResponseDto> exercisesDto = new HashSet<>();

        for (UUID exerciseId : dto.exerciseIds()) {
            Exercise exercise =
                    exerciseRepository
                            .findById(exerciseId)
                            .orElseThrow(
                                    () ->
                                            new AppBadException(
                                                    "Exercise is not found with id: "
                                                            + exerciseId));

            exercises.add(exercise);

            ExerciseResponseDto exerciseDto =
                    ExerciseResponseDto.builder()
                            .id(exercise.id())
                            .name(exercise.name())
                            .description(exercise.description())
                            .category(exercise.category())
                            .muscleGroup(exercise.muscleGroup())
                            .repetition(exercise.repetition())
                            .set(exercise.set())
                            .weight(exercise.weight())
                            .build();
            exercisesDto.add(exerciseDto);
        }

        WorkoutPlan workoutPlan =
                WorkoutPlan.builder()
                        .status(WorkoutStatus.PENDING)
                        .scheduledDateTime(dto.scheduledDateTime())
                        .owner(user)
                        .exercises(exercises)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .createdBy(user)
                        .lastModifiedBy(user)
                        .build();

        WorkoutPlan saved = repository.save(workoutPlan);

        WorkoutPlanResponseDto responseDto =
                WorkoutPlanResponseDto.builder()
                        .id(saved.id())
                        .status(saved.status())
                        .scheduledDateTime(saved.scheduledDateTime())
                        .ownerId(saved.owner().id())
                        .exercises(exercisesDto)
                        .createdAt(saved.createdAt())
                        .build();

        return AppResponse.builder()
                .success(true)
                .message("Workout plan successfully created")
                .data(responseDto)
                .build();
    }

    // get user's all workoutPlans
    @Override
    public AppResponse getAll(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<WorkoutPlan> allWorkoutPlans = repository.findByOwnerId(userId, pageable);

        return AppResponse.builder()
                .success(true)
                .message("Workout plans of page " + page)
                .data(allWorkoutPlans)
                .build();
    }

    // get all users' all workoutPlans for only admins
    @Override
    public AppResponse getAllForAdmins(int page, int size) {
        Page<WorkoutPlan> allWorkoutPlans =
                repository.findAll(
                        PageRequest.of(page - 1, size, Sort.by("createdAt").descending()));

        return AppResponse.builder()
                .success(true)
                .message("Workout plans of page " + page)
                .data(allWorkoutPlans)
                .build();
    }
}
