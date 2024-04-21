package com.example.authorization_service.entity.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * The UserWithCredentials class wraps a User entity with Spring Security UserDetails interface implementation.
 * It provides user details such as username, password, and authorities for authentication and authorization purposes.
 */
public class UserWithCredentials implements UserDetails {
    /**
     * The User entity associated with this UserDetails wrapper.
     */
    private final User user;

    /**
     * Constructs a new UserWithCredentials object with the specified User entity.
     *
     * @param user The User entity to wrap.
     */
    public UserWithCredentials(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     *
     * @return A collection of granted authorities for the user.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The user's password.
     */

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return The user's login (username).
     */
    @Override
    public String getUsername() {
        return user.getLogin();
    }

    /**
     * Indicates whether the user's account has not expired.
     *
     * @return true if the user's account is valid (i.e., not expired), false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have not expired.
     *
     * @return true if the user's credentials are valid (i.e., not expired), false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns the User entity associated with this UserDetails wrapper.
     *
     * @return The User entity.
     */
    public User getUser() {
        return user;
    }
}
