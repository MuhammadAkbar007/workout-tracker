package uz.akbar.workoutTracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.repository.UserRepository;

import java.util.Optional;

/** CustomUserDetailsService */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);

        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("user by " + username + " username not found");
        }

        User user = optional.get();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return userDetails;
    }
}
