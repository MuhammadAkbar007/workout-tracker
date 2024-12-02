package uz.akbar.workoutTracker.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import uz.akbar.workoutTracker.payload.JwtDto;
import uz.akbar.workoutTracker.security.CustomUserDetails;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

/** JwtProvider */
@Component
@Slf4j
public class JwtProviderDynamicVersion {

    private final long expiryTime = 1000 * 3600 * 24; // 1-day
    private final String secretKey = generateSecretKey();

    // private final String secretKey =
    // "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";

    public JwtDto generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", userDetails.getUsername());
        extraClaims.put(
                "roles",
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiryTime);

        String token =
                Jwts.builder()
                        .claims(extraClaims)
                        .subject(authentication.getName())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiry))
                        .signWith(getSignInKey())
                        .compact();

        return new JwtDto(token, expiry);
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parser().verifyWith((SecretKey) getSignInKey()).build().parse(token);
        return true;
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private String generateSecretKey() {
        int length = 32;
        byte[] keyBytes = new byte[length];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        return Base64.getEncoder().encodeToString(keyBytes);
    }
}
