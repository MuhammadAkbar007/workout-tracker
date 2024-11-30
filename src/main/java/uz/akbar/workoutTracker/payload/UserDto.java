package uz.akbar.workoutTracker.payload;

import uz.akbar.workoutTracker.entity.Role;

import java.util.Set;
import java.util.UUID;

/** UserDto */
public record UserDto(UUID id, String username, String email, Set<Role> roles) {}
