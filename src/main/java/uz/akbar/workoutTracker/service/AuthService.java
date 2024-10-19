package uz.akbar.workoutTracker.service;

import org.springframework.stereotype.Service;

import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.RegisterDto;

/**
 * AuthService
 */
@Service
public interface AuthService {

	ApiResponse registerUser(RegisterDto dto);

}
