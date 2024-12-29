package uz.akbar.workoutTracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.Exercise;
import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.ExerciseCategory;
import uz.akbar.workoutTracker.enums.GeneralStatus;
import uz.akbar.workoutTracker.enums.MuscleGroup;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.repository.ExerciseRepository;
import uz.akbar.workoutTracker.repository.RoleRepository;
import uz.akbar.workoutTracker.repository.UserRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** DataInitializer */
@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            ExerciseRepository exerciseRepository) {
        return args -> {
            // create userRole if doesnt exist
            Role userRole =
                    roleRepository
                            .findByRoleType(RoleType.ROLE_USER)
                            .orElseGet(
                                    () -> {
                                        Role role = new Role();
                                        role.setRoleType(RoleType.ROLE_USER);

                                        return roleRepository.save(role);
                                    });

            // create adminRole if doesnt exist
            Role adminRole =
                    roleRepository
                            .findByRoleType(RoleType.ROLE_ADMIN)
                            .orElseGet(
                                    () -> {
                                        Role role = new Role();
                                        role.setRoleType(RoleType.ROLE_ADMIN);

                                        return roleRepository.save(role);
                                    });

            // create admin & user User if doesnt exist
            if (!userRepository.existsByEmailOrUsername("akbarjondev007@gmail.com", "akbar007")) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                User user =
                        User.builder()
                                .email("akbarjondev007@gmail.com")
                                .password(passwordEncoder.encode("root123"))
                                .username("akbar007")
                                .status(GeneralStatus.ACTIVE)
                                .roles(roles)
                                .build();

                userRepository.save(user);

                if (exerciseRepository.count() <= 0) {
                    Exercise running =
                            Exercise.builder()
                                    .name("running")
                                    .description("Running 1 kilometers.")
                                    .category(ExerciseCategory.CARDIO)
                                    .muscleGroup(MuscleGroup.LEGS)
                                    .set(1)
                                    .createdAt(Instant.now())
                                    .updatedAt(Instant.now())
                                    .createdBy(user)
                                    .lastModifiedBy(user)
                                    .build();

                    Exercise benchPress =
                            Exercise.builder()
                                    .name("benchPress")
                                    .description("Bench press exercise for chest.")
                                    .category(ExerciseCategory.STRENGTH)
                                    .muscleGroup(MuscleGroup.CHEST)
                                    .set(3)
                                    .repetition(7)
                                    .weight(90.5)
                                    .createdAt(Instant.now())
                                    .updatedAt(Instant.now())
                                    .createdBy(user)
                                    .lastModifiedBy(user)
                                    .build();

                    Exercise catCowStretch =
                            Exercise.builder()
                                    .name("catCowStretch")
                                    .description(
                                            "Cat-cow stretching exercise for flexibility of back.")
                                    .category(ExerciseCategory.FLEXIBILITY)
                                    .muscleGroup(MuscleGroup.BACK)
                                    .set(3)
                                    .createdAt(Instant.now())
                                    .updatedAt(Instant.now())
                                    .createdBy(user)
                                    .lastModifiedBy(user)
                                    .build();

                    exerciseRepository.saveAll(List.of(running, benchPress, catCowStretch));
                }
            }
        };
    }
}
