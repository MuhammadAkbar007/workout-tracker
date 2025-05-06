package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.repository.RoleRepository;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.service.AdminService;

import java.util.UUID;

/** AdminServiceImpl */
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public AppResponse blockUnblockUser(UUID userId, GeneralStatus status, UUID adminId) {
		if (userId.equals(adminId))
			throw new AppBadException("Wrong id: " + adminId);

		User user = repository
				.findById(userId)
				.orElseThrow(
						() -> new AppBadException("User not found with id = " + userId));

		user.setStatus(status);
		repository.save(user);

		return AppResponse.builder()
				.success(true)
				.message(
						"User has been "
								+ (status == GeneralStatus.BLOCK ? "blocked" : "activated"))
				.data(userId)
				.build();
	}

	@Override
	@Transactional
	public AppResponse assignRole(UUID userId, RoleType roleType, UUID adminId) {
		if (userId.equals(adminId))
			throw new AppBadException("Wrong id: " + adminId);

		User user = repository
				.findById(userId)
				.orElseThrow(
						() -> new AppBadException("User not found with id = " + userId));

		switch (roleType) {
			case ROLE_ADMIN:
				Role roleAdmin = roleRepository
						.findByRoleType(RoleType.ROLE_ADMIN)
						.orElseThrow(() -> new AppBadException("Admin role is not found"));

				if (!user.getRoles().contains(roleAdmin))
					user.getRoles().add(roleAdmin);
				break;
			case ROLE_USER:
				user.getRoles().removeIf(role -> role.getRoleType().equals(RoleType.ROLE_ADMIN));
				break;

			default:
				throw new AppBadException("Wrong role type");
		}

		repository.save(user);

		return AppResponse.builder()
				.success(true)
				.message(
						"User role has been changed to "
								+ (roleType.equals(RoleType.ROLE_USER) ? " user" : " admin"))
				.data(userId)
				.build();
	}
}
