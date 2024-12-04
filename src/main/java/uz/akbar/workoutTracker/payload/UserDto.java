package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

/** UserDto */
@Builder
public record UserDto(UUID id, String username, String email, Set<RoleDto> roles) {}
