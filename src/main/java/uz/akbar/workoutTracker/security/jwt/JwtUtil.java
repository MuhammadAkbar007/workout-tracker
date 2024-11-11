package uz.akbar.workoutTracker.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import uz.akbar.workoutTracker.payload.JwtDto;

/** JwtUtil */
public class JwtUtil {

  private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
  private static final String secretKey =
      "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";

  public static String encode(String username, String role) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("username", username);
    extraClaims.put("role", role);

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenLiveTime))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public static JwtDto decode(String token) {
    Claims claims =
        Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    String username = (String) claims.get("username");
    String role = (String) claims.get("role");
    return new JwtDto(username, role);
  }

  private static Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
