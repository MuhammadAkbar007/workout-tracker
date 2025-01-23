package uz.akbar.workoutTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.service.AdminService;

import java.util.UUID;

/** AdminController */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private AdminService service;

    /*
    /api/v1/admin/block/userId?status=BLOCK
    or
    /api/v1/admin/block/userId?status=ACTIVE
    */
    @PutMapping("/block/{userId}")
    public ResponseEntity<?> blockUnblockUser(
            @PathVariable UUID userId, @RequestParam GeneralStatus status) {

        AppResponse response = service.blockUnblockUser(userId, status);
        return ResponseEntity.ok(response);
    }
}
