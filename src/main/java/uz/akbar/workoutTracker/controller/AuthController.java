package uz.akbar.workoutTracker.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RefreshTokenRequestDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.service.AuthService;

/** AuthController */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto dto) {
        AppResponse response = service.registerUser(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logInToSystem(@Valid @RequestBody LogInDto dto) {
        AppResponse response = service.logIn(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto dto) {
        AppResponse response = service.refreshToken(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequestDto dto) {
        service.logout(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
