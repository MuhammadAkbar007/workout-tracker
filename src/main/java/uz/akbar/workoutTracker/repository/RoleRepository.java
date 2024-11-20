package uz.akbar.workoutTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.enums.RoleType;

import java.util.Optional;
import java.util.UUID;

/** RoleRepository */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleType(RoleType roleType);
}
