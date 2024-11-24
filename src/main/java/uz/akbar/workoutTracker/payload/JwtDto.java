package uz.akbar.workoutTracker.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/** JwtDto */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {

    private String token;

    private Instant expiryDate;
}
