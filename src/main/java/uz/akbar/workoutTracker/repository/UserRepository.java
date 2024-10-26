package uz.akbar.workoutTracker.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.akbar.workoutTracker.entity.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmailOrUsername(String email, String username);

	Optional<User> findByUsername(String username);
}
