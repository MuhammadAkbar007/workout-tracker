package uz.akbar.workoutTracker.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.RefreshToken;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.JwtDto;
import uz.akbar.workoutTracker.repository.RefreshTokenRepository;
import uz.akbar.workoutTracker.security.CustomUserDetails;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

/** JwtProvider */
@Component
public class JwtProvider {
    private final Key jwtSecretKey;
    private final long jwtAccessTokenExpiration;
    private final long jwtRefreshTokenExpiration;
    private final int MAX_ACTIVE_TOKENS_PER_USER = 5;

    @Autowired private RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtAccessTokenExpiration = accessTokenExpiration;
        this.jwtRefreshTokenExpiration = refreshTokenExpiration;
    }

    public JwtDto generateAccessToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(jwtAccessTokenExpiration);

        String token =
                Jwts.builder()
                        .subject(userDetails.getUsername())
                        .claim("userId", userDetails.getUserId().toString())
                        .claim("authorities", userDetails.getAuthorities())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiryDate))
                        .signWith(jwtSecretKey)
                        .compact();

        return JwtDto.builder().token(token).expiryDate(expiryDate).build();
    }

    @Transactional
    public JwtDto generateRefreshToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(jwtRefreshTokenExpiration);

        User user = userDetails.getUser();
        long activeTokenCount = refreshTokenRepository.countByUserAndExpiryDateAfter(user, now);

        if (activeTokenCount >= MAX_ACTIVE_TOKENS_PER_USER) {
            RefreshToken oldestToken =
                    refreshTokenRepository.findFirstByUserOrderByCreatedAtAsc(user).orElse(null);

            if (oldestToken != null) refreshTokenRepository.delete(oldestToken);
        }

        String token =
                Jwts.builder()
                        .subject(userDetails.getUsername())
                        .claim("userId", userDetails.getUserId().toString())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiryDate))
                        .signWith(jwtSecretKey)
                        .compact();

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(token)
                        .expiryDate(expiryDate)
                        .createdAt(Instant.now())
                        .user(user)
                        .build();

        refreshTokenRepository.save(refreshToken);

        return JwtDto.builder().token(token).expiryDate(expiryDate).build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) jwtSecretKey).build().parse(token);
            return true;
        } catch (SecurityException ex) {
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getUserIdFromToken(String token) {
        Claims claims =
                Jwts.parser()
                        .verifyWith((SecretKey) jwtSecretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return claims.get("userId", String.class);
    }
}
