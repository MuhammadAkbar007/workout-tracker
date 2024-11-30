package uz.akbar.workoutTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.RefreshToken;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.repository.RefreshTokenRepository;
import uz.akbar.workoutTracker.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/** RefreshTokenService */
@Transactional
@Service
public class RefreshTokenService {

    private final int MAX_ACTIVE_TOKENS_PER_USER = 5;
    private final long expiryTime = 1000 * 3600 * 24 * 3; // 3-day

    @Autowired private RefreshTokenRepository repository;
    @Autowired private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new AppBadException("Username: " + username + " not found"));

        long activeTokenCount = repository.countByUserAndExpiryDateAfter(user, Instant.now());
        if (activeTokenCount >= MAX_ACTIVE_TOKENS_PER_USER) {

            RefreshToken oldestToken =
                    repository.findFirstByUserOrderByCreatedAtAsc(user).orElse(null);

            if (oldestToken != null) repository.delete(oldestToken);
        }

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .user(user)
                        .expiryDate(Instant.now().plusMillis(expiryTime))
                        .createdAt(Instant.now())
                        .token(UUID.randomUUID().toString())
                        .build();

        return repository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public RefreshToken verifyRefreshToken(RefreshToken token) {
        if (token.expiryDate().compareTo(Instant.now()) < 0) {
            repository.delete(token);
            throw new AppBadException("Refresh token is expired. Please make a new login..!");
        }

        return token;
    }

    public void deleteByUser(User user) {
        userRepository.findById(user.id()).ifPresent(repository::deleteByUser);
    }
}
