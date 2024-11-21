package uz.akbar.workoutTracker.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import uz.akbar.workoutTracker.entity.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** UserDto */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private String username;

    private String email;

    private Set<Role> roles = new HashSet<>();
}
