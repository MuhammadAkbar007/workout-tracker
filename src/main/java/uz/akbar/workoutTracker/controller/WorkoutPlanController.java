package uz.akbar.workoutTracker.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;
import uz.akbar.workoutTracker.security.CustomUserDetails;
import uz.akbar.workoutTracker.service.WorkoutPlanService;

import java.util.UUID;

/** WorkoutPlanController */
@RestController
@RequestMapping("/api/v1/workoutplan")
public class WorkoutPlanController {

    @Autowired private WorkoutPlanService service;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> create(
            @Valid @RequestBody WorkoutPlanDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        AppResponse response = service.create(dto, userDetails.getUser());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // only admin can see all users' workoutplans
    public ResponseEntity<?> getAllForAdmins(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        AppResponse response = service.getAllForAdmins(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('USER')") // user's only his/her own workoutplans
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        AppResponse response = service.getAll(userDetails.getUserId(), page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getById(
            @PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        AppResponse response = service.getById(id, userDetails.getUser());
        return ResponseEntity.ok(response);
    }
    // @PutMapping("/mine")
    // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<?> update()
}
