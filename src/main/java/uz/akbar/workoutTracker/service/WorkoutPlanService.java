package uz.akbar.workoutTracker.service;


import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;

/** WorkoutPlanService */
public interface WorkoutPlanService {

    AppResponse create(WorkoutPlanDto dto, User user);
}
