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
import uz.akbar.workoutTracker.repository.RefreshTokenRepository;
import uz.akbar.workoutTracker.repository.RoleRepository;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.security.CustomUserDetails;
import uz.akbar.workoutTracker.security.jwt.JwtProvider;
import uz.akbar.workoutTracker.service.AuthService;

import java.time.Instant;
import java.util.Set;

/** AuthServiceImpl */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository repository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AppResponse registerUser(RegisterDto dto) {
        if (repository.existsByEmailOrUsername(dto.email(), dto.username()))
            throw new AppBadException("User already exists");

        Role role =
                roleRepository
                        .findByRoleType(RoleType.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role User is not found"));

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
                UserDto.builder()
                        .id(saved.id())
                        .username(saved.username())
                        .email(saved.email())
                        .roles(saved.roles())
                        .build();

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

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        JwtDto accessTokenDto = jwtProvider.generateAccessToken(authentication);
        JwtDto refreshTokenDto = jwtProvider.generateRefreshToken(authentication);

        JwtResponseDto jwtResponseDto =
                JwtResponseDto.builder()
                        .username(user.username())
                        .accessToken(accessTokenDto.token())
                        .refreshToken(refreshTokenDto.token())
                        .accessTokenExpiryTime(accessTokenDto.expiryDate())
                        .refreshTokenExpiryTime(refreshTokenDto.expiryDate())
                        .build();

        return AppResponse.builder()
                .success(true)
                .message("User successfully logged in")
                .data(jwtResponseDto)
                .build();
    }

    @Override
    @Transactional
    public AppResponse refreshToken(RefreshTokenRequestDto dto) {
        String refreshToken = dto.refreshToken();

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new RefreshTokenException("Invalid refresh token");
        }

        RefreshToken storedToken =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(
                                () ->
                                        new RefreshTokenException(
                                                "Refresh token is not in database!"));

        if (storedToken.expiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RefreshTokenException("Refresh token has expired");
        }

        String username = jwtProvider.getUsernameFromToken(refreshToken);

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        JwtDto accessTokenDto = jwtProvider.generateAccessToken(authentication);
        JwtDto refreshTokenDto = jwtProvider.generateRefreshToken(authentication);

        JwtResponseDto jwtResponseDto =
                JwtResponseDto.builder()
                        .username(username)
                        .accessToken(accessTokenDto.token())
                        .refreshToken(refreshTokenDto.token())
                        .accessTokenExpiryTime(accessTokenDto.expiryDate())
                        .refreshTokenExpiryTime(refreshTokenDto.expiryDate())
                        .build();

        return AppResponse.builder()
                .success(true)
                .message("Tokens successfully regenerated")
                .data(jwtResponseDto)
                .build();
    }

    @Override
    @Transactional
    public AppResponse logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() != "anonymousUser"
                && authentication.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            User user = userDetails.getUser();

            refreshTokenRepository.deleteByUser(user);

            SecurityContextHolder.clearContext();
        }

        return AppResponse.builder().success(true).message("User successfully logged out").build();
    }
}
