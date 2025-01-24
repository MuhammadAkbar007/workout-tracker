package uz.akbar.workoutTracker.service;

import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.payload.AppResponse;

import java.util.UUID;

/** AdminService */
public interface AdminService {

    AppResponse blockUnblockUser(UUID userId, GeneralStatus status, UUID adminId);

    AppResponse assignRole(UUID userId, RoleType roleType, UUID adminId);
}
