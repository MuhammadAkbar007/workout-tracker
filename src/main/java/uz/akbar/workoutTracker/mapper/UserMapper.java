package uz.akbar.workoutTracker.mapper;

import org.mapstruct.Mapper;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.UserDto;

/** UserMapper */
@Mapper(componentModel = "spring", uses = { RoleMapper.class, WorkoutPlanMapper.class })
public interface UserMapper {

	UserDto toDto(User user);
}
