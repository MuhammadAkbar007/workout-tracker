package uz.akbar.workoutTracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import uz.akbar.workoutTracker.entity.Role;
import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.RoleType;
import uz.akbar.workoutTracker.repository.RoleRepository;
import uz.akbar.workoutTracker.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

/** DataInitializer */
@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
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

                User user = new User();
                user.setEmail("akbarjondev007@gmail.com");
                user.setPassword(passwordEncoder.encode("root123"));
                user.setUsername("akbar007");
                user.setRoles(roles);

                userRepository.save(user);
            }
        };
    }
}
