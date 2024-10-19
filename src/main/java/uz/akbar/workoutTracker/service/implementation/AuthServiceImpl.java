package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.service.AuthService;

/**
 * AuthServiceImpl
 */
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepository repository;

	@Override
	public ApiResponse registerUser(RegisterDto dto) {
		if (repository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())) {
			return new ApiResponse(false, "User already exists");
		}

		// TODO: true apiresponse
		return new ApiResponse(true, "ok");
	}

}
