package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RegisterDto;

/**
 * AuthService
 */
public interface AuthService {

	ApiResponse registerUser(RegisterDto dto);

	ApiResponse logIn(LogInDto dto);
}
