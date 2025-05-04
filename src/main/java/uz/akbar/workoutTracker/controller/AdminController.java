package uz.akbar.workoutTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.security.CustomUserDetails;
import uz.akbar.workoutTracker.service.AdminService;

import java.util.UUID;

/** AdminController */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private AdminService service;

	/*
	 * /api/v1/admin/block/userId?status=BLOCK
	 * or
	 * /api/v1/admin/block/userId?status=ACTIVE
	 */
	@PutMapping("/block/{userId}")
	public ResponseEntity<?> blockUnblockUser(
			@PathVariable UUID userId,
			@RequestParam(required = true) GeneralStatus status,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		AppResponse response = service.blockUnblockUser(userId, status, userDetails.getUserId());
		return ResponseEntity.ok(response);
	}

	/*
	 * /api/v1/admin/assign/userId?roleType=ROLE_ADMIN
	 * or
	 * /api/v1/admin/assign/userId?roleType=ROLE_USER
	 */
	@PutMapping("/assign/{userId}")
	public ResponseEntity<?> assignRole(
			@PathVariable UUID userId,
			@RequestParam RoleType roleType,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		AppResponse response = service.assignRole(userId, roleType, userDetails.getUserId());
		return ResponseEntity.ok(response);
	}
}
