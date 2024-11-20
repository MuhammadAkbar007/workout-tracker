package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.AuthResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RegisterDto;

/** AuthService */
public interface AuthService {

    User registerUser(RegisterDto dto);

    AuthResponseDto logIn(LogInDto dto);
}
