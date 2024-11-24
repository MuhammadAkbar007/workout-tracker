package uz.akbar.workoutTracker.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/** AuthResponseDto */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenExpiryTime;

    private Instant refreshTokenExpiryTime;
}
