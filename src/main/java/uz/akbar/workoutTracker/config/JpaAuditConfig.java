package uz.akbar.workoutTracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.security.CustomUserDetails;

import java.util.Optional;

/** JpaAuditConfig */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

	@Bean
	public AuditorAware<User> auditorProvider() {
		return () -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null
					|| !authentication.isAuthenticated()
					|| "anonymousUser".equals(authentication.getPrincipal())) {
				return Optional.empty();
			}

			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

			return Optional.of(customUserDetails.getUser());
		};
	}
}
