package uz.akbar.workoutTracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import uz.akbar.workoutTracker.entity.WorkoutPlan;
import uz.akbar.workoutTracker.payload.WorkoutPlanResponseDto;

/** WorkoutPlanMapper */
@Mapper(componentModel = "spring")
public interface WorkoutPlanMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    WorkoutPlanResponseDto toDto(WorkoutPlan workoutPlan);
}
