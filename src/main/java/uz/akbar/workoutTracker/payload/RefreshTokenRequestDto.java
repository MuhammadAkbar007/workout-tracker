package uz.akbar.workoutTracker.payload;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** RefreshTokenRequestDto */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenRequestDto {

    @NotBlank(message = "token is required")
    private String refreshToken;
}
