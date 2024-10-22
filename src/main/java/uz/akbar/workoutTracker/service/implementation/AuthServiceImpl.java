package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.service.AuthService;

/**
 * AuthServiceImpl
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepository repository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public ApiResponse registerUser(RegisterDto dto) {
		if (repository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())) {
			return new ApiResponse(false, "User already exists");
		}

		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		try {
			User saved = repository.save(user);
			return new ApiResponse(true, saved.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse(false, "Error during save to database");
		}

	}

}
