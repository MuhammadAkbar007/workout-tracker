package uz.akbar.workoutTracker.mapper;

import org.mapstruct.Mapper;

import uz.akbar.workoutTracker.entity.Exercise;
import uz.akbar.workoutTracker.payload.ExerciseResponseDto;

/** ExerciseMapper */
@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseResponseDto toDto(Exercise exercise);
}
