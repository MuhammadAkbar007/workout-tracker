package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;

import java.util.UUID;

/** WorkoutPlanService */
public interface WorkoutPlanService {

    AppResponse create(WorkoutPlanDto dto, User user);

    AppResponse getAll(UUID userId, int page, int size);

    AppResponse getAllForAdmins(int page, int size);

    AppResponse getById(UUID id, User user);
}
