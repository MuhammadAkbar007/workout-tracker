package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.RegisterDto;

/**
 * AuthService
 */
public interface AuthService {

	ApiResponse registerUser(RegisterDto dto);

}
