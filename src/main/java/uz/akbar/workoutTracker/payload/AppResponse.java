package uz.akbar.workoutTracker.payload;

import lombok.Builder;

/** AppResponse */
@Builder
public record AppResponse(Boolean success, String message, Object data) {}
