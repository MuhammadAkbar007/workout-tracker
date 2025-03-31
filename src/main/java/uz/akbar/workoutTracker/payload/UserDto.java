package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import uz.akbar.workoutTracker.enums.GeneralStatus;

import java.util.Set;
import java.util.UUID;

/** UserDto */
@Builder
public record UserDto(
		UUID id,
		String username,
		String email,
		GeneralStatus status,
		Set<RoleDto> roles,
		Set<WorkoutPlanResponseDto> workoutPlans) {
}
