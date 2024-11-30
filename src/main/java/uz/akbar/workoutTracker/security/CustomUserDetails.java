package uz.akbar.workoutTracker.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import uz.akbar.workoutTracker.entity.User;
import uz.akbar.workoutTracker.enums.GeneralStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** CustomUserDetails */
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        user.roles().stream()
                .map(role -> roles.add(new SimpleGrantedAuthority(role.roleType().name())));

        return roles;
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public String getUsername() {
        return user.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.status() != GeneralStatus.BLOCK;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
