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
import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.exception.RefreshTokenException;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.JwtDto;
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
    public AppResponse registerUser(RegisterDto dto) {
        if (repository.existsByEmailOrUsername(dto.email(), dto.username()))
            throw new AppBadException("User already exists");

        Optional<Role> optionalRole = roleRepository.findByRoleType(RoleType.ROLE_USER);
        Role role = optionalRole.orElseThrow(() -> new RuntimeException("Role User is not found"));

        User user =
                User.builder()
                        .username(dto.username())
                        .email(dto.email())
                        .roles(Set.of(role))
                        .password(passwordEncoder.encode(dto.password()))
                        .status(GeneralStatus.ACTIVE)
                        .build();
        User saved = repository.save(user);

        UserDto userDto =
                new UserDto(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRoles());

        return AppResponse.builder()
                .success(true)
                .message("User successfully registered")
                .data(userDto)
                .build();
    }

    @Override
    public AppResponse logIn(LogInDto dto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (!authentication.isAuthenticated())
            throw new AppBadException("User is not authenticated");

        JwtDto accessTokenObject = jwtUtil.generateToken(authentication);
        RefreshToken refreshTokenObject = refreshTokenService.createRefreshToken(dto.username());

        JwtResponseDto jwtResponseDto =
                JwtResponseDto.builder()
                        .accessToken(accessTokenObject.token())
                        .refreshToken(refreshTokenObject.getToken())
                        .accessTokenExpiryTime(accessTokenObject.expiryDate())
                        .refreshTokenExpiryTime(refreshTokenObject.getExpiryDate())
                        .build();

        return AppResponse.builder()
                .success(true)
                .message("User successfully logged in")
                .data(jwtResponseDto)
                .build();
    }

    @Override
    public AppResponse refreshToken(RefreshTokenRequestDto dto) {

        RefreshToken token =
                refreshTokenService
                        .findByToken(dto.refreshToken())
                        .orElseThrow(
                                () ->
                                        new RefreshTokenException(
                                                "Refresh token is not in database!"));

        refreshTokenService.verifyRefreshToken(token);

        User user = token.getUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated())
            throw new AppBadException("User is not authenticated");

        JwtDto accessTokenObject = jwtUtil.generateToken(authentication);
        RefreshToken refreshTokenObject =
                refreshTokenService.createRefreshToken(user.getUsername());

        JwtResponseDto jwtResponseDto =
                JwtResponseDto.builder()
                        .accessToken(accessTokenObject.token())
                        .refreshToken(refreshTokenObject.getToken())
                        .accessTokenExpiryTime(accessTokenObject.expiryDate())
                        .refreshTokenExpiryTime(refreshTokenObject.getExpiryDate())
                        .build();

        return AppResponse.builder()
                .success(true)
                .message("Tokens successfully regenerated")
                .data(jwtResponseDto)
                .build();
    }
}
