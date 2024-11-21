package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.payload.AuthResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.payload.UserDto;

/** AuthService */
public interface AuthService {

    UserDto registerUser(RegisterDto dto);

    AuthResponseDto logIn(LogInDto dto);
}
