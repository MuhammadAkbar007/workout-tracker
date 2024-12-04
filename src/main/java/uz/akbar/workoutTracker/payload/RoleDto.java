package uz.akbar.workoutTracker.payload;

import lombok.Builder;

import uz.akbar.workoutTracker.enums.RoleType;

import java.util.UUID;

/** RoleDto */
@Builder
public record RoleDto(UUID id, RoleType roleType) {}
