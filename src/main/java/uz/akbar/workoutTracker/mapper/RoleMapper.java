package uz.akbar.workoutTracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.payload.RoleDto;

/** RoleMapper */
@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDto dto);

    RoleDto toDto(Role role);
}
