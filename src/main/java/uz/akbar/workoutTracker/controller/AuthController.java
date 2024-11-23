package uz.akbar.workoutTracker.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.payload.JwtResponseDto;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RefreshTokenRequestDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.payload.UserDto;
import uz.akbar.workoutTracker.service.AuthService;

/** AuthController */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto dto) {
        UserDto userDto = service.registerUser(dto);
        return ResponseEntity.status(201).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logInToSystem(@Valid @RequestBody LogInDto dto) {
        JwtResponseDto jwtResponseDto = service.logIn(dto);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto dto) {
        JwtResponseDto jwResponseDto = service.refreshToken(dto);
        return ResponseEntity.ok(jwResponseDto);
    }
}
