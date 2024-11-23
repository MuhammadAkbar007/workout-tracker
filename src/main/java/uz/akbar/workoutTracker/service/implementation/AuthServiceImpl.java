package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.RefreshToken;
import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.exception.RefreshTokenException;
import uz.akbar.workoutTracker.payload.JwtResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RefreshTokenRequestDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.payload.UserDto;
import uz.akbar.workoutTracker.repository.RoleRepository;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.security.jwt.JwtUtil;
import uz.akbar.workoutTracker.service.AuthService;
import uz.akbar.workoutTracker.service.RefreshTokenService;

import java.util.Optional;
import java.util.Set;

/** AuthServiceImpl */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository repository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public UserDto registerUser(RegisterDto dto) {
        if (repository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername()))
            throw new AppBadException("User already exists");

        Optional<Role> optionalRole = roleRepository.findByRoleType(RoleType.ROLE_USER);
        Role role = optionalRole.orElseThrow(() -> new RuntimeException("Role User is not found"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User saved = repository.save(user);

        UserDto userDto =
                new UserDto(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRoles());

        return userDto;
    }

    @Override
    public JwtResponseDto logIn(LogInDto dto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(), dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (!authentication.isAuthenticated())
            throw new AppBadException("User is not authenticated");

        RefreshToken refreshTokenObject = refreshTokenService.createRefreshToken(dto.getUsername());
        String refreshToken = refreshTokenObject.getToken();
        String accessToken = jwtUtil.generateToken(authentication);

        return new JwtResponseDto(accessToken, refreshToken);
    }

    @Override
    public JwtResponseDto refreshToken(RefreshTokenRequestDto dto) {

        RefreshToken token =
                refreshTokenService
                        .findByToken(dto.getRefreshToken())
                        .orElseThrow(
                                () ->
                                        new RefreshTokenException(
                                                "Refresh token is not in database!"));

        refreshTokenService.verifyRefreshToken(token);

        User user = token.getUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated())
            throw new AppBadException("User is not authenticated");

        RefreshToken refreshTokenObject =
                refreshTokenService.createRefreshToken(user.getUsername());
        String refreshToken = refreshTokenObject.getToken();
        String accessToken = jwtUtil.generateToken(authentication);

        return new JwtResponseDto(accessToken, refreshToken);
    }
}
