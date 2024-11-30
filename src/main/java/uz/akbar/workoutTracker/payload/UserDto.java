package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import uz.akbar.workoutTracker.entity.Role;

import java.util.Set;
import java.util.UUID;

/** UserDto */
@Builder
public record UserDto(UUID id, String username, String email, Set<Role> roles) {}
