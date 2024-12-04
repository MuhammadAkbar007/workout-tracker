package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RefreshTokenRequestDto;
import uz.akbar.workoutTracker.payload.RegisterDto;

/** AuthService */
public interface AuthService {

    AppResponse registerUser(RegisterDto dto);

    AppResponse logIn(LogInDto dto);

    AppResponse refreshToken(RefreshTokenRequestDto dto);

    void logout(RefreshTokenRequestDto dto);
}
