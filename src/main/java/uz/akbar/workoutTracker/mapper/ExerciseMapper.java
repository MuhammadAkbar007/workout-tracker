package uz.akbar.workoutTracker.mapper;

import org.mapstruct.Mapper;

import uz.akbar.workoutTracker.entity.Exercise;
import uz.akbar.workoutTracker.payload.ExerciseResponseDto;

import java.util.List;
import java.util.Set;

/** ExerciseMapper */
@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseResponseDto toDto(Exercise exercise);

    Set<Exercise> toSet(List<Exercise> exercises);
}
