package uz.akbar.workoutTracker.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** AuthResponseDto */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {

    private String accessToken;

    private String refreshToken;
}
