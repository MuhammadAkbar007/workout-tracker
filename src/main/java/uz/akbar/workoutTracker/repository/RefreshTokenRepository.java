package uz.akbar.workoutTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.akbar.workoutTracker.entity.RefreshToken;
import uz.akbar.workoutTracker.entity.User;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/** RefreshTokenRepository */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    long countByUserAndExpiryDateAfter(User user, Instant now);

    Optional<RefreshToken> findFirstByUserOrderByCreatedAtAsc(User user);
}
