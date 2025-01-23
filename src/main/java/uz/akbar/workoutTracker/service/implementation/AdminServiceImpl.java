package uz.akbar.workoutTracker.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.exception.AppBadException;
import uz.akbar.workoutTracker.payload.AppResponse;
import uz.akbar.workoutTracker.repository.UserRepository;
import uz.akbar.workoutTracker.service.AdminService;

import java.util.UUID;

/** AdminServiceImpl */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired private UserRepository repository;

    @Override
    public AppResponse blockUnblockUser(UUID userId, GeneralStatus status) {
        User user =
                repository
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
}
