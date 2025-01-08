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
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.enums.WorkoutStatus;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.mapper.ExerciseMapper;
import uz.akbar.workoutTracker.mapper.WorkoutPlanMapper;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.ExerciseResponseDto;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;
import uz.akbar.workoutTracker.payload.WorkoutPlanResponseDto;
import uz.akbar.workoutTracker.payload.WorkoutPlanUpdateDto;
import uz.akbar.workoutTracker.repository.ExerciseRepository;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.repository.WorkoutPlanRepository;
import uz.akbar.workoutTracker.service.WorkoutPlanService;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** WorkoutPlanServiceImpl */
@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    @Autowired private WorkoutPlanRepository repository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ExerciseMapper exerciseMapper;
    @Autowired private WorkoutPlanMapper mapper;

    private boolean determineIsAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getRoleType() == RoleType.ROLE_ADMIN);
    }

    @Override
    @Transactional
    public AppResponse create(WorkoutPlanDto dto, User user) {
        if (dto.scheduledDateTime().isBefore(Instant.now()))
            throw new AppBadException("Schedule is not for future");

        if (dto.exerciseIds() == null || dto.exerciseIds().isEmpty()) {
            throw new AppBadException("At least one exercise should be selected");
        }

        Set<Exercise> exercises = new HashSet<>();
        Set<ExerciseResponseDto> exercisesDtos = new HashSet<>();

        // fetching exercises from database
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

            ExerciseResponseDto exerciseDto = exerciseMapper.toDto(exercise);

            exercisesDtos.add(exerciseDto);
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

        WorkoutPlanResponseDto responseDto = mapper.toDto(saved);

        return AppResponse.builder()
                .success(true)
                .message("Workout plan successfully created")
                .data(responseDto)
                .build();
    }

    // get all users' all workoutPlans for only admins
    /*     @Override
       @Transactional(readOnly = true)
       public AppResponse getAllForAdmins(int page, int size) {
           Page<WorkoutPlan> allWorkoutPlans =
                   repository.findAll(
                           PageRequest.of(page - 1, size, Sort.by("createdAt").descending()));

           Page<WorkoutPlanResponseDto> response = allWorkoutPlans.map(mapper::toDto);

           return AppResponse.builder()
                   .success(true)
                   .message("Workout plans of page " + page)
                   .data(response)
                   .build();
        }
    */

    // get user's all workoutPlans
    /* @Override
       @Transactional(readOnly = true)
       public AppResponse getAll(UUID userId, int page, int size) {
           Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

           Page<WorkoutPlan> allWorkoutPlans = repository.findByOwnerId(userId, pageable);

           Page<WorkoutPlanResponseDto> response = allWorkoutPlans.map(mapper::toDto);

           return AppResponse.builder()
                   .success(true)
                   .message("Workout plans of page " + page)
                   .data(response)
                   .build();
       }
    */

    @Override
    @Transactional(readOnly = true)
    public AppResponse getAll(int page, int size, User user) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<WorkoutPlan> workoutPlans;

        if (determineIsAdmin(user)) {
            workoutPlans = repository.findAll(pageable);
        } else {
            workoutPlans = repository.findByOwnerId(user.getId(), pageable);
        }

        Page<WorkoutPlanResponseDto> response = workoutPlans.map(mapper::toDto);

        return AppResponse.builder()
                .success(true)
                .message("Workout plans of page " + page)
                .data(response)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AppResponse getById(UUID id, User user) {
        WorkoutPlan workoutPlan;

        if (determineIsAdmin(user)) {
            workoutPlan =
                    repository
                            .findById(id)
                            .orElseThrow(() -> new AppBadException("Workout plan not found"));
        } else {
            workoutPlan =
                    repository
                            .findByIdAndOwnerId(id, user.getId())
                            .orElseThrow(() -> new AppBadException("Workout plan not found"));
        }

        return AppResponse.builder()
                .success(true)
                .message("Workout plan retrieved successfully")
                .data(mapper.toDto(workoutPlan))
                .build();
    }

    @Override
    @Transactional
    public AppResponse update(UUID id, WorkoutPlanUpdateDto dto, User user) {
        WorkoutPlan workoutPlan;
        boolean isAdmin = determineIsAdmin(user);

        if (dto.scheduledDateTime().isBefore(Instant.now()))
            throw new AppBadException("Schedule is not for future");

        if (isAdmin) {
            workoutPlan =
                    repository
                            .findById(id)
                            .orElseThrow(() -> new AppBadException("Workout plan not found"));
        } else {
            workoutPlan =
                    repository
                            .findByIdAndOwnerId(id, user.getId())
                            .orElseThrow(() -> new AppBadException("Workout plan not found"));
        }

        workoutPlan.setStatus(dto.status());
        workoutPlan.setScheduledDateTime(dto.scheduledDateTime());

        if (isAdmin && dto.ownerId() != null) {
            User owner =
                    userRepository
                            .findById(dto.ownerId())
                            .orElseThrow(() -> new AppBadException("User not found"));

            workoutPlan.setOwner(owner);
        }

        if (dto.exerciseIds() != null && !dto.exerciseIds().isEmpty()) {
            List<Exercise> exercises = exerciseRepository.findAllById(dto.exerciseIds());

            if (exercises.size() != dto.exerciseIds().size())
                throw new AppBadException("One or more exercises not foun");

            workoutPlan.setExercises(exerciseMapper.toSet(exercises));
        }

        WorkoutPlan saved = repository.save(workoutPlan);

        return AppResponse.builder()
                .success(true)
                .message("Workout plan updated successfully")
                .data(mapper.toDto(saved))
                .build();
    }

    @Override
    @Transactional
    public AppResponse delete(UUID id, User user) {
        if (determineIsAdmin(user)) {
            repository.deleteById(id);
        } else {
            int deletedCount = repository.deleteByIdAndOwnerId(id, user.getId());

            if (deletedCount == 0)
                throw new AppBadException(
                        "Workout plan not found or you don't have permission to delete it");
        }

        return AppResponse.builder()
                .success(true)
                .message("Workout plan successfully deleted")
                .build();
    }
}
