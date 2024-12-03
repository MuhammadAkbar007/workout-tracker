package uz.akbar.workoutTracker.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.payload.WorkoutPlanDto;
import uz.akbar.workoutTracker.security.CustomUserDetails;
import uz.akbar.workoutTracker.service.WorkoutPlanService;

/** WorkoutPlanController */
@RestController
@RequestMapping("/api/v1/workoutplan")
public class WorkoutPlanController {

    @Autowired private WorkoutPlanService service;

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody WorkoutPlanDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        AppResponse response = service.create(dto, userDetails.getUser());
        return ResponseEntity.status(201).body(response);
    }
}
