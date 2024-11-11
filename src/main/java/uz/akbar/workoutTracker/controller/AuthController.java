package uz.akbar.workoutTracker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.akbar.workoutTracker.payload.ApiResponse;
import uz.akbar.workoutTracker.payload.LogInDto;
import uz.akbar.workoutTracker.payload.RegisterDto;
import uz.akbar.workoutTracker.service.implementation.AuthServiceImpl;

/** AuthController */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired private AuthServiceImpl service;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto dto) {
    ApiResponse response = service.registerUser(dto);
    return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response.getObject());
  }

  @PostMapping("/login")
  public ResponseEntity<?> logInToSystem(@Valid @RequestBody LogInDto dto) {
    System.out.println(dto);
    return ResponseEntity.ok("OK");
  }
}
