package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.payload.JwtResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RefreshTokenRequestDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.payload.UserDto;

/** AuthService */
public interface AuthService {

    UserDto registerUser(RegisterDto dto);

    JwtResponseDto logIn(LogInDto dto);

    JwtResponseDto refreshToken(RefreshTokenRequestDto dto);
}
