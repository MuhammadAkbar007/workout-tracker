package uz.akbar.workoutTracker.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

/** JwtUtil */
@Component
public class JwtUtil {

    private final long expirationTime = 1000 * 3600 * 24; // 1-day
    private final String secretKey = generateSecretKey();

    // private final String secretKey =
    // "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";

    public String generateToken(Authentication authentication) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", authentication.getPrincipal());
        extraClaims.put("roles", authentication.getAuthorities());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
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
