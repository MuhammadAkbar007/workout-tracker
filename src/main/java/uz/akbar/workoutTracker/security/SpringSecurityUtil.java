package uz.akbar.workoutTracker.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uz.akbar.workoutTracker.entity.User;

/**
 * SpringSecurityUtil
 */
public class SpringSecurityUtil {

	public static User getCurrentEntity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		return customUserDetails.getUser();
	}

	public static UUID getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		return customUserDetails.getUser().getId();
	}
}
