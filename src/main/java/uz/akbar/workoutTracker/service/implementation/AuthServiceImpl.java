package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.payload.AuthResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.security.jwt.JwtUtil;
import uz.akbar.workoutTracker.service.AuthService;

import java.util.Set;

/** AuthServiceImpl */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository repository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    @Override
    @Transactional
    public User registerUser(RegisterDto dto) {
        if (repository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())) {
            throw new AppBadException("User already exists");
        }

        Role role = new Role();
        role.setRoleType(RoleType.ROLE_USER);

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return repository.save(user);
    }

    @Override
    public AuthResponseDto logIn(LogInDto dto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(), dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authentication);

        return new AuthResponseDto(token);
    }
}
